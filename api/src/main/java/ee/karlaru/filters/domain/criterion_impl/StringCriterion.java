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

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Data
@Entity
@Component
@SuperBuilder
@RequiredArgsConstructor
@DiscriminatorValue("STRING")
@EqualsAndHashCode(callSuper = true)
public class StringCriterion extends Criterion {

    @JsonIgnore
    public static final String NAME = "STRING";

    public enum Condition {
        BEGINS_WITH,
        NOT_BEGINS_WITH,
        CONTAINS,
        NOT_CONTAINS,
        ENDS_WITH,
        NOT_ENDS_WITH;
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
        return switch (condition) {
            case BEGINS_WITH -> criteriaBuilder.like(root.get(getTargetField()), getTargetValue() + "%");
            case NOT_BEGINS_WITH -> criteriaBuilder.notLike(root.get(getTargetField()), getTargetValue() + "%");
            case CONTAINS -> criteriaBuilder.like(root.get(getTargetField()), "%" + getTargetValue() + "%");
            case NOT_CONTAINS -> criteriaBuilder.notLike(root.get(getTargetField()), "%" + getTargetValue() + "%");
            case ENDS_WITH -> criteriaBuilder.like(root.get(getTargetField()), "%" + getTargetValue());
            case NOT_ENDS_WITH -> criteriaBuilder.notLike(root.get(getTargetField()), "%" + getTargetValue());
        };
    }

    @Override
    @Transient
    public String getName(){
        return NAME;
    }

}

