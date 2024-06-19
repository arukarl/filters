package ee.karlaru.filters.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final TokenVerifier tokenVerifier;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken bearer = (BearerTokenAuthenticationToken) authentication;
        UsernamePasswordAuthenticationToken token;

        try {
            token = getAuthentication(bearer.getToken());
        } catch (Exception e) {
            throw new InvalidBearerTokenException("Failed to verify JWT token", e);
        }
        return token;

    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        List<String> roles = tokenVerifier.getRoles(token);
        List<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
        return new UsernamePasswordAuthenticationToken(token, null, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
