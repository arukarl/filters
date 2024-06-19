package ee.karlaru.filters.specification;

import ee.karlaru.filters.domain.Criterion;
import ee.karlaru.filters.domain.Movie;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.List;


public class MovieSpecification implements Specification<Movie> {

    private final transient List<Criterion> filters;

    public MovieSpecification(List<Criterion> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Movie> root, @NonNull CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate[] predicates = filters.stream()
                .map(filter -> filter.toPredicate(root, criteriaBuilder))
                .toArray(Predicate[]::new);
        return criteriaBuilder.and(predicates);
    }
}
