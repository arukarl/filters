package ee.karlaru.filters.exception;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FilterNotFoundException extends RuntimeException {

    public FilterNotFoundException(String message) {
        super(message);
    }
}
