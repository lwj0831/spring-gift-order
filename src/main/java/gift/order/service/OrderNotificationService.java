package gift.order.service;

import gift.auth.exception.KakaoReauthenticationRequiredException;
import gift.auth.service.KakaoTokenService;
import gift.infra.kakao.KakaoApiClient;
import gift.infra.kakao.exception.KakaoApiClientException;
import gift.infra.kakao.exception.KakaoApiServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationService {

    private final KakaoTokenService kakaoTokenService;
    private final KakaoApiClient kakaoApiClient;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderNotificationService(KakaoTokenService kakaoTokenService,
        KakaoApiClient kakaoApiClient) {
        this.kakaoTokenService = kakaoTokenService;
        this.kakaoApiClient = kakaoApiClient;
    }

    public void sendOrderCompleteNotification(Long memberId, Long orderId) {
        try {
            String kakaoAccessToken = kakaoTokenService.getValidAccessTokenOrRefresh(memberId);
            kakaoApiClient.sendKakaoMessage(kakaoAccessToken, "주문이 완료되었습니다. 주문번호: ORD-" + orderId);

        } catch (KakaoReauthenticationRequiredException e) {
            logger.warn("카카오 재인증 필요로 메시지 전송 실패. 주문 ID: {}, 회원 ID: {}", orderId, memberId);

        } catch (KakaoApiClientException | KakaoApiServerException e) {
            logger.warn("카카오 API 호출 예외로 메세지 전송 실패. 주문 ID: {}, 회원 ID: {}, 오류: {}",
                orderId, memberId, e.getMessage(), e);
        }
    }

}
