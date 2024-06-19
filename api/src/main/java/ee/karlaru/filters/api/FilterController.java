package ee.karlaru.filters.api;

import ee.karlaru.filters.domain.Filter;
import ee.karlaru.filters.service.FilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/filter")
public class FilterController {

    private final FilterService filterService;


    @PatchMapping
    @PreAuthorize("hasRole('ROLE_WRITE')")
    @Operation(security = { @SecurityRequirement(name = "WRITE role JWT token") })
    public UUID updateFilter(@RequestBody @Valid Filter filter) {
        return filterService.updateFilter(filter);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_READ')")
    @Operation(security = { @SecurityRequirement(name = "READ role JWT token") })
    public Iterable<Filter> getFilters() {
        return filterService.getFilters();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_READ')")
    @Operation(security = { @SecurityRequirement(name = "READ role JWT token") })
    public Filter getFilter(@PathVariable UUID id) {
        return filterService.getFilter(id);
    }

}
