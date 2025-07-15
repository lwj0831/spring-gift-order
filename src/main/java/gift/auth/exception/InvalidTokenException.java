package gift.auth.exception;

import gift.global.exception.BusinessException;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException() {
        super(AuthErrorCode.INVALID_TOKEN);
    }

}
