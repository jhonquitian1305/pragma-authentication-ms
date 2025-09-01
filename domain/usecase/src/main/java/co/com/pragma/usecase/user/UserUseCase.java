package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.user.validation.UserValidation;
import co.com.pragma.usecase.user.exception.DniExistsException;
import co.com.pragma.usecase.user.exception.EmailExistsException;
import co.com.pragma.usecase.user.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository userRepository;

    @Override
    public Mono<User> saveOne(User user){
        UserValidation.validate(user);
        return this.userRepository.isUniqueEmail(user.getEmail())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new EmailExistsException("El email ingresado ya se encuentra registrado")))
                .flatMap(notExists -> this.userRepository.existsByDni(user.getDni()))
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new DniExistsException("El dni ingresado ya se encuentra registrado")))
                .flatMap(notExists ->  this.userRepository.saveOne(user));
    }

    @Override
    public Mono<User> getByDni(String dni) {
        return this.userRepository.getByDni(dni)
                .switchIfEmpty(Mono.error(new NotFoundException("User with dni %s not found".formatted(dni))));
    }
}
