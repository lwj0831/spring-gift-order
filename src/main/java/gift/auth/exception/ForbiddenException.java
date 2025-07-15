package gift.auth.exception;

import gift.global.exception.BusinessException;

public class ForbiddenException extends BusinessException {

    public ForbiddenException() {
        super(AuthErrorCode.FORBIDDEN);
    }
}
