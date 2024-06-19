package ee.karlaru.filters.api.response;

public record ErrorResponse (
        String message,
        String code
){}
