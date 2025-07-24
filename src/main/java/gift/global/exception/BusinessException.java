package gift.global.exception;

import java.util.HashMap;
import java.util.Map;

public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> arguments;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.arguments = new HashMap<>();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.arguments = new HashMap<>();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    protected void addArgument(String key, Object value) {
        arguments.put(key, value);
    }
}
