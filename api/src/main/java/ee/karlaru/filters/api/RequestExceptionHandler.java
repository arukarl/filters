package ee.karlaru.filters.api;

import ee.karlaru.filters.api.response.ErrorResponse;
import ee.karlaru.filters.exception.FilterNotFoundException;
import ee.karlaru.filters.exception.InvalidCriterionException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.security.auth.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class RequestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String REQUEST_ATTRIBUTE_NAME = "REQUEST_ERROR";


    @ExceptionHandler(value = {AuthenticationException.class, AccessDeniedException.class, AuthorizationDeniedException.class, InvalidBearerTokenException.class})
    public ResponseEntity<Object> handleAuthenticationException(Exception e, WebRequest request) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), "ACCESS_DENIED");
        log.error("Access denied", e);
        return handleExceptionInternal(e, response, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler({FilterNotFoundException.class, ConstraintViolationException.class})
    public ResponseEntity<Object> handleFilterNotFoundException(FilterNotFoundException e, WebRequest request) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), "FILTER_NOT_FOUND");
        log.error("Filter not found", e);
        return handleExceptionInternal(e, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {InvalidCriterionException.class})
    public ResponseEntity<Object> handleUnprocessableCriterionException(InvalidCriterionException e, WebRequest request) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), "INVALID_CRITERION");
        log.error("Invalid criterion", e);
        return handleExceptionInternal(e, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleUnknownException(Exception ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse("An unexpected error occurred", "INTERNAL_ERROR");
        log.error("An unexpected error occurred", ex);
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex,
                                                             Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatusCode statusCode,
                                                             WebRequest request) {
        request.setAttribute(REQUEST_ATTRIBUTE_NAME, body, RequestAttributes.SCOPE_REQUEST);
        log.error("Error occurred", ex);
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

}
