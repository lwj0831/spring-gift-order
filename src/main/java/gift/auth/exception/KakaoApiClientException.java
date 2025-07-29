package gift.auth.exception;

import gift.global.exception.BusinessException;

public class KakaoApiClientException extends BusinessException {

    public KakaoApiClientException(String text) {
        super(AuthErrorCode.KAKAO_CLIENT_ERROR);
        addArgument("status text", text);
    }
}
