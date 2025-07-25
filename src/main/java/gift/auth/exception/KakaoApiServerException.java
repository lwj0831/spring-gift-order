package gift.auth.exception;

import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;

public class KakaoApiServerException extends BusinessException {

    public KakaoApiServerException(String text) {
        super(AuthErrorCode.KAKAO_SERVER_ERROR);
        addArgument("status text",text);
    }
}
