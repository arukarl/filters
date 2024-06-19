package ee.karlaru.filters.service;

import ee.karlaru.filters.domain.Filter;
import ee.karlaru.filters.domain.Movie;
import ee.karlaru.filters.exception.FilterNotFoundException;
import ee.karlaru.filters.repository.FilterRepository;
import ee.karlaru.filters.repository.MovieRepository;
import ee.karlaru.filters.specification.MovieSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static ee.karlaru.filters.config.RedisConfig.FILTERED_MOVIES_CACHE;
import static ee.karlaru.filters.config.RedisConfig.MOVIES_CACHE;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final FilterRepository filterRepository;


    @Cacheable(FILTERED_MOVIES_CACHE)
    public List<Movie> getMoviesWithFilters(UUID filterUuid) {
        log.info("Fetching movies with filter {}", filterUuid);
        Filter filter = filterRepository.findByUuid(filterUuid)
                .orElseThrow(() -> new FilterNotFoundException("Filter %s not found".formatted(filterUuid)));

        MovieSpecification spec = new MovieSpecification(filter.getCriteria());
        return movieRepository.findAll(spec);
    }

    @Cacheable(MOVIES_CACHE)
    public List<Movie> getMovies() {
        log.info("Fetching all movies");
        return movieRepository.findAll();
    }

    @CacheEvict(MOVIES_CACHE)
    public void addMovie(Movie movie) {
        log.info("Adding a movie");
        movieRepository.save(movie);
    }
}
