package co.com.pragma.security.config;

import co.com.pragma.security.filter.JwtFilter;
import co.com.pragma.security.repository.SecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final SecurityContextRepository securityContextRepository;

    private static final Logger logger =  Logger.getLogger(SecurityConfig.class.getName());

    public SecurityConfig(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http, JwtFilter jwtFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchangeSpec -> exchangeSpec
                        .pathMatchers("/api/v1/login/**").permitAll()
                        .pathMatchers("/swagger-ui/**").permitAll()
                        .pathMatchers("/v3/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/users/**").hasAnyAuthority("ADMIN", "ASESOR")
                        .pathMatchers(HttpMethod.GET, "/api/v1/users/{dni}").hasAnyAuthority("CLIENTE")
                        .anyExchange().authenticated())
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
                .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
                .securityContextRepository(securityContextRepository)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .build();
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);


            String body = """
                {
                  "error": "Access Denied",
                  "message": "No tienes permisos para acceder a este recurso",
                  "status": 403
                }
                """;
            logger.info(body);

            DataBuffer buffer = exchange.getResponse().bufferFactory()
                    .wrap(body.getBytes(StandardCharsets.UTF_8));

            return exchange.getResponse().writeWith(Mono.just(buffer));
        };
    }

    @Bean
    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, ex) -> {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            String body = """
                {
                  "error": "Unauthorized",
                  "message": "Debes autenticarte para acceder a este recurso",
                  "status": 401
                }
                """;
            logger.info(body);

            DataBuffer buffer = exchange.getResponse().bufferFactory()
                    .wrap(body.getBytes(StandardCharsets.UTF_8));

            return exchange.getResponse().writeWith(Mono.just(buffer));
        };
    }
}
