package pl.gov.cmp.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;
import pl.gov.cmp.administration.exception.InvitationExpiredException;
import pl.gov.cmp.administration.exception.InvitationNotFoundException;
import pl.gov.cmp.cemetery.exception.CemeteryNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    private static final String NOT_FOUND = "Not found";
    private final MessageSource messageSource;

    @ExceptionHandler({AppRollbackException.class})
    public ResponseEntity<ErrorResponse> handleRollbackException(AppRollbackException exception, HttpServletRequest request, Locale locale) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        String message = this.getMessage(exception.getCode(), exception.getMessage(), locale);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(exception.getCode(), message, uuid));
    }

    @ExceptionHandler({AppNoRollbackException.class})
    public ResponseEntity<ErrorResponse> handleNoRollbackException(AppNoRollbackException exception, HttpServletRequest request, Locale locale) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        String message = this.getMessage(exception.getCode(), exception.getMessage(), locale);
        return ResponseEntity
                .status(UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(exception.getCode(), message, uuid));
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleException(NoSuchElementException exception, HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        final HttpStatus statusCode = HttpStatus.NOT_FOUND;
        String message = this.getMessage(exception.getMessage(), NOT_FOUND);
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponse(statusCode.value(), message, uuid));
    }

    @ExceptionHandler({CemeteryNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleException(AppRollbackException exception, HttpServletRequest request, Locale locale) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        String message = this.getMessage(exception.getCode(), exception.getMessage(), NOT_FOUND, locale);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getCode(), message, uuid));
    }

    @ExceptionHandler({InvitationNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleException(InvitationNotFoundException exception, HttpServletRequest request, Locale locale) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        String message = this.getMessage(exception.getCode(), exception.getMessage(), NOT_FOUND, locale);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getCode(), message, uuid));
    }

    @ExceptionHandler({InvitationExpiredException.class})
    public ResponseEntity<ErrorResponse> handleException(InvitationExpiredException exception, HttpServletRequest request, Locale locale) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        String message = this.getMessage(exception.getCode(), exception.getMessage(), NOT_FOUND, locale);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(exception.getCode(), message, uuid));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        final HttpStatus statusCode = BAD_REQUEST;
        String message = exception.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(statusCode.value(), message, uuid));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        final HttpStatus statusCode = BAD_REQUEST;
        String message = this.getMessage(exception.getMessage(), "Illegal argument");
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponse(statusCode.value(), message, uuid));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleException(ConstraintViolationException exception, HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        final HttpStatus statusCode = BAD_REQUEST;
        String message = exception.getConstraintViolations().stream()
                .map(this::getErrorMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(statusCode.value(), message, uuid));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException exception, HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        final HttpStatus statusCode = BAD_REQUEST;
        String message = this.getMessage(exception.getMessage(), "Illegal argument");
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponse(statusCode.value(), message, uuid));
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ErrorResponse> handleException(BindException exception) {
        UUID uuid = UUID.randomUUID();
        log.error(uuid.toString(), exception);
        final HttpStatus statusCode = BAD_REQUEST;
        String message = exception.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(statusCode.value(), message, uuid));
    }

    @ExceptionHandler({SecurityException.class})
    public ResponseEntity<ErrorResponse> handleException(SecurityException exception, HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        final HttpStatus statusCode = FORBIDDEN;
        String message = this.getMessage(exception.getMessage(), "Forbidden");
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponse(statusCode.value(), message, uuid));
    }

    @ExceptionHandler({InternalApplicationException.class})
    public ResponseEntity<ErrorResponse> handleException(InternalApplicationException exception) {
        final var uuid = UUID.randomUUID();
        log.error(uuid.toString(), exception);
        final var statusCode = INTERNAL_SERVER_ERROR;
        final var message = this.getMessage(exception.getMessage(), "Internal application exception");
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponse(statusCode.value(), message, uuid));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        logError(exception, request, uuid);
        final HttpStatus statusCode = INTERNAL_SERVER_ERROR;
        String message = this.getMessage(exception.getMessage(), "General error");
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponse(statusCode.value(), message, uuid));
    }

    private void logError(Exception exception, HttpServletRequest request, UUID uuid) {
        final Map<String, ArrayList<String>> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(request.getHeaders(h))
                ));
        String content = "";
        if (request instanceof ContentCachingRequestWrapper && log.isDebugEnabled()) {
            content = new String(((ContentCachingRequestWrapper) request).getContentAsByteArray());
        }
        log.debug("request: {}", request);
        log.error("UUID: " + uuid.toString() + ", URI: {}, headers: {}, content: {}", request.getRequestURI(), headers, content, exception);
    }

    private String getErrorMessage(ConstraintViolation<?> error) {
        return error.getMessage();
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            FieldError err = (FieldError) error;
            return err.getField() + ": " + err.getDefaultMessage();
        }
        return error.getObjectName() + ": " + error.getDefaultMessage();
    }

    private String getMessage(String message, String defaultMessage) {
        return Optional.ofNullable(message).orElse(defaultMessage);
    }

    private String getMessage(int errorCode, String message, Locale locale, String... params) {
        return this.getMessage(errorCode, message, "Undefined error", locale, params);
    }

    private String getMessage(int errorCode, String message, String defaultMessage, Locale locale, String... params) {
        try {
            return this.messageSource.getMessage("errorCode." + errorCode, params, locale);
        } catch (Exception e) {
            return Optional.ofNullable(message).orElse(defaultMessage);
        }
    }
}
