package ee.karlaru.filters.domain.criterion_impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.karlaru.filters.domain.Criterion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Data
@Entity
@Component
@SuperBuilder
@RequiredArgsConstructor
@DiscriminatorValue("DATE")
@EqualsAndHashCode(callSuper = true)
public class DateCriterion extends Criterion {

    @JsonIgnore
    public static final String NAME = "DATE";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    public enum Condition {
        AFTER,
        NOT_BEFORE,
        EQUAL,
        NOT_AFTER,
        BEFORE;
    }

    @Override
    public Map<String, List<String>> toClassification() {
        return Map.of(NAME, Arrays
                .stream(Condition.values())
                .map(Enum::name)
                .toList());
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaBuilder criteriaBuilder) {
        Condition condition = Condition.valueOf(getOperator());
        LocalDate dateValue = LocalDate.parse(getTargetValue(), DATE_FORMATTER);
        return switch (condition) {
            case AFTER -> criteriaBuilder.greaterThan(root.get(getTargetField()), dateValue);
            case NOT_BEFORE -> criteriaBuilder.greaterThanOrEqualTo(root.get(getTargetField()), dateValue);
            case EQUAL -> criteriaBuilder.equal(root.get(getTargetField()), dateValue);
            case NOT_AFTER -> criteriaBuilder.lessThanOrEqualTo(root.get(getTargetField()), dateValue);
            case BEFORE -> criteriaBuilder.lessThan(root.get(getTargetField()), dateValue);
        };
    }

    @Override
    @Transient
    public String getName(){
        return NAME;
    }

}
