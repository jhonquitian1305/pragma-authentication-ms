package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.authentication.Login;
import co.com.pragma.model.user.authentication.Token;
import co.com.pragma.model.user.exception.BusinessException;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import co.com.pragma.security.provider.*;

@Repository
@Slf4j
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User,
    UserEntity,
    Long,
    UserReactiveRepository
> implements UserRepository {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper,
                                         PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<User> saveOne(User user) {
        log.info("saving a user");
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return super.save(user);
    }

    @Override
    public Mono<Boolean> isUniqueEmail(String email) {
        log.info("searching if email is unique");
        return this.repository.existsByEmail(email);
    }

    @Override
    public Mono<User> getByDni(String dni) {
        return this.repository.findByDni(dni)
                .map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByDni(String dni) {
        return this.repository.existsByDni(dni);
    }

    @Override
    public Mono<Token> login(Login login) {
        return this.repository.findByEmail(login.email())
                .filter(userDocument -> passwordEncoder.matches(login.password(), userDocument.getPassword()))
                .map(userDocument -> new Token(jwtProvider.generateToken(userDocument)));
    }

    @Override
    public Mono<User> getInfoByDni(String dni) {
        return this.repository.findByDni(dni)
                .map(this::toEntity);
    }
}
