package ee.karlaru.filters.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ee.karlaru.filters.properties.SecurityProperties;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TokenVerifier {

    private final JWTVerifier jwtVerifier;

    public TokenVerifier(SecurityProperties securityProperties) {
        Algorithm secretKey = Algorithm.HMAC384(securityProperties.getJwtSecretKey());
        this.jwtVerifier = JWT.require(secretKey)
                .withIssuer(securityProperties.getJwtIssuer())
                .acceptExpiresAt(securityProperties.getJwtExpirationInMinutes() * 60L)
                .build();
    }

    public List<String> getRoles(@NonNull String accessToken) {
        log.debug("Verifying access token");
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(accessToken);
            return decodedJWT.getClaim("ROLE").asList(String.class);
        } catch (Exception e) {
            log.error("Failed to verify access token", e);
            throw new InvalidBearerTokenException("Failed to verify access token");
        }
    }
}
