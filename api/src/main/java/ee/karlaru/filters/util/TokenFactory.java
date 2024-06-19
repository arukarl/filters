package ee.karlaru.filters.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ee.karlaru.filters.properties.SecurityProperties;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenFactory {

    private final Algorithm algorithm;
    private final int expirationInMinutes;
    private final String issuer;

    public TokenFactory(SecurityProperties securityProperties) {
        this.algorithm = Algorithm.HMAC384(securityProperties.getJwtSecretKey());
        this.expirationInMinutes = securityProperties.getJwtExpirationInMinutes();
        this.issuer = securityProperties.getJwtIssuer();
    }

    public String create(String[] userRoles) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject("AskEnd")
                .withArrayClaim("ROLE", userRoles)
                .withExpiresAt(Instant.now().plusSeconds(60L + expirationInMinutes))
                .sign(algorithm);
    }
}
