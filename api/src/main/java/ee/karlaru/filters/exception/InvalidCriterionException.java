package ee.karlaru.filters.exception;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class InvalidCriterionException extends RuntimeException {

    public InvalidCriterionException(String message) {
        super(message);
    }
}
