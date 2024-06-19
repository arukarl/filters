package ee.karlaru.filters.api;

import ee.karlaru.filters.domain.Classifications;
import ee.karlaru.filters.service.FilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ClassificationController {

    private final FilterService filterService;


    @GetMapping("/classifications")
    @PreAuthorize("hasRole('ROLE_READ')")
    @Operation(security = { @SecurityRequirement(name = "READ role JWT token") })
    public Classifications getClassifications() {
        return filterService.getClassifications();
    }

}
