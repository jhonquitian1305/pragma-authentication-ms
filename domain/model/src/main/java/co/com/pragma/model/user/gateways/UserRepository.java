package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> saveOne(User user);
    Mono<Boolean> isUniqueEmail(String email);

    Mono<User> getByDni(String dni);
}
