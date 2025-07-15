package gift.global.exception;

import gift.global.exception.dto.ErrorResponse;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public class ErrorResponseFactory {

    public static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode) {
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.from(errorCode, errorCode.getErrorMessage(), null));
    }

    public static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode,
        String message) {
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.from(errorCode, message, null));
    }

    public static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode,
        Map<String, Object> additionalInfo) {
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.from(errorCode, errorCode.getErrorMessage(), additionalInfo));
    }

    public static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode,
        String message, Map<String, Object> additionalInfo) {
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.from(errorCode, message, additionalInfo));
    }

    public static ResponseEntity<ErrorResponse> createErrorResponse(BusinessException exception) {
        return ResponseEntity
            .status(exception.getErrorCode().getStatus())
            .body(ErrorResponse.from(exception.getErrorCode(), exception.getMessage(),
                exception.getArguments()));
    }
}
