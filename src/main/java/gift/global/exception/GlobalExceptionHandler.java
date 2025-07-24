package gift.global.exception;

import gift.global.exception.dto.ErrorResponse;
import gift.global.utils.PropertyPathUtils;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception) {
        logger.error("MethodArgumentNotValidException. occurred: {}", exception.getMessage(),
            exception);

        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        Map<String, Object> extras = new HashMap<>();
        if (!globalErrors.isEmpty()) {
            extras.put("global-errors", globalErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList());
        }
        if (!fieldErrors.isEmpty()) {
            extras.put("invalid-params", fieldErrors.stream()
                .map(err -> Map.of(
                    "name", err.getField(),
                    "value", String.valueOf(err.getRejectedValue()),
                    "reason", err.getDefaultMessage()))
                .toList());
        }

        return ErrorResponseFactory.createErrorResponse(GlobalErrorCode.INVALID_ARGUMENT_ERROR,
            extras);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
        ConstraintViolationException exception) {
        logger.error("ConstraintViolationException occurred: {}", exception.getMessage(),
            exception);

        List<Map<String, String>> invalidParams = exception.getConstraintViolations().stream()
            .map(violation -> Map.of(
                "name", PropertyPathUtils.extractFieldName(violation.getPropertyPath().toString()),
                "value", String.valueOf(violation.getInvalidValue()),
                "reason", violation.getMessage()
            ))
            .toList();

        Map<String, Object> additionalInfo = Map.of("invalid-params", invalidParams);

        return ErrorResponseFactory.createErrorResponse(GlobalErrorCode.INVALID_ARGUMENT_ERROR,
            additionalInfo);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException exception) {
        logger.error("IllegalArgumentException occurred: {}", exception.getMessage(), exception);

        return ErrorResponseFactory.createErrorResponse(GlobalErrorCode.INVALID_ARGUMENT_ERROR,
            exception.getMessage());
    }
}
