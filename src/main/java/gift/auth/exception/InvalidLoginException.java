package gift.auth.exception;

import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;

public class InvalidLoginException extends BusinessException {

    public InvalidLoginException() {
        super(AuthErrorCode.INVALID_LOGIN_EXCEPTION);
    }
}
