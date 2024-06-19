package ee.karlaru.filters.util;

import ee.karlaru.filters.domain.Classifications;
import ee.karlaru.filters.domain.Filter;
import ee.karlaru.filters.exception.InvalidCriterionException;

public class CriteriaValidator {

    public static void validate(Classifications classifications, Filter filter) {

        filter.getCriteria().stream().parallel()
                .forEach(criterion -> {

                    Classifications.Criterion field = classifications.criteria().stream()
                            .filter(f -> f.fieldName().equals(criterion.getTargetField()))
                            .findFirst()
                            .orElseThrow(() -> new InvalidCriterionException("Unknown field " + criterion.getTargetField()));

                    if (!field.operators().contains(criterion.getOperator())) {
                        throw new InvalidCriterionException("Unknown operator " + criterion.getOperator());
                    }
                }
                );
    }

    private CriteriaValidator() {
    }
}
