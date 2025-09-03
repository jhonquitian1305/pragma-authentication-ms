package co.com.pragma.security.manager;

import co.com.pragma.security.provider.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(auth -> auth.getCredentials() != null)
                .map(auth -> auth.getCredentials().toString())
                .flatMap(token -> {
                    if (!jwtProvider.validate(token)) {
                        return Mono.empty();
                    }

                    Claims claims = jwtProvider.getClaims(token);

                    return Mono.just(new UsernamePasswordAuthenticationToken(
                            jwtProvider.getSubject(token), // aquí usas el subject (username)
                            null,
                            Stream.of(claims.get("roles"))
                                    .map(role -> (List<Map<String, String>>) role)
                                    .flatMap(role -> role.stream()
                                            .map(r -> r.get("authority"))
                                            .map(SimpleGrantedAuthority::new))
                                    .toList()
                    ));
                });
    }
}
