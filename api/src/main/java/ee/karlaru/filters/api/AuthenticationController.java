package ee.karlaru.filters.api;

import ee.karlaru.filters.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @GetMapping("/read")
    public String getReadAccessToken() {
        return authenticationService.generateReadOnlyToken();
    }

    @GetMapping("/write")
    public String getWriteAccessToken() {
        return authenticationService.generateWriteToken();
    }

}
