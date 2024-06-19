package ee.karlaru.filters.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Data
@Validated
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    @NotBlank
    private String jwtSecretKey;

    @NotNull
    private Integer jwtExpirationInMinutes;

    @NotBlank
    private String jwtIssuer;

}
