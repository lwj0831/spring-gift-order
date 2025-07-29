package gift.auth.exception;

import gift.global.exception.BusinessException;

public class KakaoApiServerException extends BusinessException {

    public KakaoApiServerException(String text) {
        super(AuthErrorCode.KAKAO_SERVER_ERROR);
        addArgument("status text", text);
    }
}
