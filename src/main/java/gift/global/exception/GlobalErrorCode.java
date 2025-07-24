package gift.global.exception;

import org.springframework.http.HttpStatus;

public enum GlobalErrorCode implements ErrorCode {
    INVALID_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "GLOBAL-001", "요청 값이 유효하지 않습니다"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL-002", "내부 서버 오류가 발생했습니다");

    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    GlobalErrorCode(HttpStatus status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
