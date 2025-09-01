package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User,
    UserEntity,
    Long,
    UserReactiveRepository
> implements UserRepository {
    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
    }

    @Override
    public Mono<User> saveOne(User user) {
        log.info("saving a user");
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
}
