package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.authentication.Login;
import co.com.pragma.model.user.authentication.Token;
import co.com.pragma.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> saveOne(User user);
    Mono<Boolean> isUniqueEmail(String email);

    Mono<User> getByDni(String dni);

    Mono<Boolean> existsByDni(String dni);

    Mono<Token> login(Login login);

    Mono<User> getInfoByDni(String dni);
}
