package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.authentication.Login;
import co.com.pragma.model.user.authentication.Token;
import reactor.core.publisher.Mono;

public interface IUserUseCase {
    Mono<User> saveOne(User user);

    Mono<User> getByDni(String dni);

    Mono<Token> login(Login login);
}
