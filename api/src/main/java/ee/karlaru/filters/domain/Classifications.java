package ee.karlaru.filters.domain;

import java.util.List;

public record Classifications(
        List<Criterion> criteria
) {
    public record Criterion(
            String fieldName,
            String fieldType,
            List<String> operators) {
    }

}
