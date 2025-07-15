package gift.auth.exception;

import gift.global.exception.BusinessException;

public class PasswordMismatchException extends BusinessException {

    public PasswordMismatchException() {
        super(AuthErrorCode.PASSWORD_MISMATCH);
    }

}
