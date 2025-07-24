package gift.global.exception.dto;

import gift.global.exception.ErrorCode;
import java.util.Collections;
import java.util.Map;

public record ErrorResponse(
    String errorCode,
    String errorMessage,
    Map<String, Object> extras
) {

    private ErrorResponse(String errorCode, String errorMessage) {
        this(errorCode, errorMessage, Collections.emptyMap());
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getErrorCode(), errorCode.getErrorMessage());
    }

    public static ErrorResponse from(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.getErrorCode(), message);
    }

    public static ErrorResponse from(ErrorCode errorCode, Map<String, Object> extras) {
        return new ErrorResponse(errorCode.getErrorCode(), errorCode.getErrorMessage(), extras);
    }

    public static ErrorResponse from(ErrorCode errorCode, String errorMessage,
        Map<String, Object> extras) {
        return new ErrorResponse(errorCode.getErrorCode(), errorMessage, extras);
    }

}