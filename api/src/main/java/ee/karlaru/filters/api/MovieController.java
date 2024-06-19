package ee.karlaru.filters.api;

import ee.karlaru.filters.domain.Movie;
import ee.karlaru.filters.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;


    @GetMapping
    @PreAuthorize("hasRole('ROLE_READ')")
    @Operation(security = { @SecurityRequirement(name = "READ role JWT token") })
    public List<Movie> getMovies() {
        return movieService.getMovies();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_WRITE')")
    @Operation(security = { @SecurityRequirement(name = "WRITE role JWT token") })
    public void addMovie(@RequestBody @Validated Movie movie) {
        movieService.addMovie(movie);
    }

    @GetMapping("/filtered")
    @PreAuthorize("hasRole('ROLE_READ')")
    @Operation(security = { @SecurityRequirement(name = "READ role JWT token") })
    public List<Movie> getMoviesWithFilters(@RequestParam UUID filter) {
        return movieService.getMoviesWithFilters(filter);
    }

}
