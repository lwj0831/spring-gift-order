package gift.auth.exception;

import gift.global.exception.BusinessException;

public class KakaoReauthenticationRequiredException extends BusinessException {

    public KakaoReauthenticationRequiredException() {
        super(AuthErrorCode.EXPIRED_TOKEN, "카카오 재인증이 필요합니다.");
    }
}
