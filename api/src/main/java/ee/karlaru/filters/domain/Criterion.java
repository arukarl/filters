package ee.karlaru.filters.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import ee.karlaru.filters.util.CustomTypeIdResolver;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;


@Data
@Entity
@Validated
@SuperBuilder
@NoArgsConstructor
@JsonTypeIdResolver(CustomTypeIdResolver.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Criterion {

    @JsonIgnore
    public abstract String getName();
    public abstract Predicate toPredicate(Root<?> root, CriteriaBuilder criteriaBuilder);
    public abstract Map<String, List<String>> toClassification();

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String operator;

    @NotNull
    private String targetField;

    @NotNull
    private String targetValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filter_id")
    @JsonIgnore
    @JsonBackReference
    private Filter filter;

}
