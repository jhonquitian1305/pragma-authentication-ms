package co.com.pragma.security.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final Logger logger =  Logger.getLogger(JwtProvider.class.getName());

    private final TokenProperties tokenProperties;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * tokenProperties.expiration()))
                .signWith(getKey(tokenProperties.secret()))
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey(tokenProperties.secret()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey(tokenProperties.secret()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validate(String token){
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getKey(tokenProperties.secret()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return true;
        } catch (ExpiredJwtException e) {
            logger.severe("token expired");
        } catch (UnsupportedJwtException e) {
            logger.severe("token unsupported");
        } catch (MalformedJwtException e) {
            logger.severe("token malformed");
        } catch (IllegalArgumentException e) {
            logger.severe("illegal args");
        }
        return false;
    }

    private Key getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}

