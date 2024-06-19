package ee.karlaru.filters.service;

import ee.karlaru.filters.domain.UserRole;
import ee.karlaru.filters.util.TokenFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenFactory tokenFactory;

    public String generateReadOnlyToken() {
        log.info("Generating read-only token");
        String[] roles = {UserRole.READ.name()};
        return tokenFactory.create(roles);
    }

    public String generateWriteToken() {
        log.info("Generating full access token");
        String[] roles = {UserRole.READ.name(), UserRole.WRITE.name()};
        return tokenFactory.create(roles);
    }
}
