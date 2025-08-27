package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.user.validation.UserValidation;
import co.com.pragma.usecase.user.exception.EmailExistsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository userRepository;

    @Override
    public Mono<User> saveOne(User user){
        UserValidation.validate(user);
        return this.userRepository.isUniqueEmail(user.getEmail())
                .flatMap(found -> {
                    if(Boolean.TRUE.equals(found)) {
                        return Mono.error(new EmailExistsException("El email debe ser único"));
                    }

                    return this.userRepository.saveOne(user);
                });
    }
}
