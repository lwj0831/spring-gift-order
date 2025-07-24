package gift.auth.exception;

import gift.global.exception.BusinessException;

public class ExpiredTokenException extends BusinessException {

    public ExpiredTokenException() {
        super(AuthErrorCode.EXPIRED_TOKEN);
    }

}
