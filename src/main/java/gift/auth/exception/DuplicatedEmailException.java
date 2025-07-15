package gift.auth.exception;

import gift.global.exception.BusinessException;

public class DuplicatedEmailException extends BusinessException {

    public DuplicatedEmailException() {
        super(AuthErrorCode.DUPLICATED_EMAIL);
    }
}
