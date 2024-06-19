package ee.karlaru.filters.util;

import ee.karlaru.filters.domain.Filter;
import ee.karlaru.filters.domain.Movie;
import ee.karlaru.filters.domain.criterion_impl.DateCriterion;
import ee.karlaru.filters.domain.criterion_impl.NumberCriterion;
import ee.karlaru.filters.domain.criterion_impl.StringCriterion;
import ee.karlaru.filters.repository.FilterRepository;
import ee.karlaru.filters.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Component
public class TestDataFactory {

    public static final UUID FIRST_FILTER_UUID = UUID.fromString("fc54d1b4-db3c-479d-9263-5df458a1562a");
    public static final UUID SECOND_FILTER_UUID = UUID.fromString("a7c371bd-3a82-4157-b154-d6a40021681c");

    @Autowired private FilterRepository filterRepository;
    @Autowired private MovieRepository movieRepository;


    public void createTestMovies() {
        List<Movie> movies = List.of(
                Movie.builder()
                        .title("Jaws")
                        .views(100)
                        .releaseDate(Date.valueOf("1994-09-22"))
                        .build(),

                Movie.builder()
                        .title("The Godfather")
                        .views(200)
                        .releaseDate(Date.valueOf("2010-01-22"))
                        .build(),

                Movie.builder()
                        .title("The Dark Knight")
                        .views(300)
                        .releaseDate(Date.valueOf("2020-09-22"))
                        .build()
        );
        movieRepository.saveAll(movies);
    }

    public void createTestFilters() {
        List<Filter> filters = List.of(
                Filter.builder()
                        .uuid(FIRST_FILTER_UUID)
                        .criteria(
                                List.of(
                                        DateCriterion.builder()
                                                .operator(DateCriterion.Condition.BEFORE.name())
                                                .targetField("releaseDate")
                                                .targetValue("2010-01-23")
                                                .build(),

                                        NumberCriterion.builder()
                                                .operator(NumberCriterion.Condition.GREATER_THAN.name())
                                                .targetField("views")
                                                .targetValue("150")
                                                .build()
                                )
                        )
                        .build(),

                Filter.builder()
                        .uuid(SECOND_FILTER_UUID)
                        .criteria(
                                List.of(
                                        StringCriterion.builder()
                                                .operator(StringCriterion.Condition.CONTAINS.name())
                                                .targetField("title")
                                                .targetValue("movie")
                                                .build()
                                )
                        )
                        .build()
        );

        filterRepository.saveAll(filters);
    }

    public void cleanUp() {
        filterRepository.deleteAll();
        movieRepository.deleteAll();
    }
}
