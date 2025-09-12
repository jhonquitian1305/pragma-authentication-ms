package co.com.pragma.security.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record TokenProperties(
        String secret,
        Integer expiration
) {
}
