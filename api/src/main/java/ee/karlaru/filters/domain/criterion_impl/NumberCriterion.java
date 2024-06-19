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
@DiscriminatorValue("NUMBER")
@EqualsAndHashCode(callSuper = true)
public class NumberCriterion extends Criterion {

    @JsonIgnore
    public static final String NAME = "NUMBER";

    public enum Condition {
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        EQUAL,
        LESS_THAN_OR_EQUAL,
        LESS_THAN;
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
            case GREATER_THAN -> criteriaBuilder.gt(root.get(getTargetField()), Double.parseDouble(getTargetValue()));
            case GREATER_THAN_OR_EQUAL -> criteriaBuilder.ge(root.get(getTargetField()), Double.parseDouble(getTargetValue()));
            case EQUAL -> criteriaBuilder.equal(root.get(getTargetField()), Double.parseDouble(getTargetValue()));
            case LESS_THAN_OR_EQUAL -> criteriaBuilder.le(root.get(getTargetField()), Double.parseDouble(getTargetValue()));
            case LESS_THAN -> criteriaBuilder.lt(root.get(getTargetField()), Double.parseDouble(getTargetValue()));
        };
    }

    @Override
    @Transient
    public String getName(){
        return NAME;
    }
    
}