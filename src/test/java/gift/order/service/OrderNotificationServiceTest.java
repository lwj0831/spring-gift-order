package gift.order.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import gift.auth.exception.KakaoReauthenticationRequiredException;
import gift.auth.service.KakaoTokenService;
import gift.infra.kakao.KakaoApiClient;
import gift.infra.kakao.exception.KakaoApiClientException;
import gift.infra.kakao.exception.KakaoApiServerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderNotificationServiceTest {

    @Mock
    private KakaoTokenService kakaoTokenService;

    @Mock
    private KakaoApiClient kakaoApiClient;

    @InjectMocks
    private OrderNotificationService orderNotificationService;

    private final Long memberId = 1L;
    private final Long orderId = 123L;
    private final String accessToken = "access-token";

    @Test
    @DisplayName("정상적으로 카카오 메시지를 전송한다")
    void sendOrderCompleteNotification_success() {
        // given
        when(kakaoTokenService.getValidAccessTokenOrRefresh(memberId)).thenReturn(accessToken);

        // when
        assertDoesNotThrow(() ->
            orderNotificationService.sendOrderCompleteNotification(memberId, orderId)
        );

        // then
        verify(kakaoTokenService).getValidAccessTokenOrRefresh(memberId);
        verify(kakaoApiClient).sendKakaoMessage(accessToken, "주문이 완료되었습니다. 주문번호: ORD-" + orderId);
    }

    @Test
    @DisplayName("KakaoReauthenticationRequiredException이 발생해도 예외를 던지지 않는다")
    void sendOrderCompleteNotification_reauthException() {
        // given
        when(kakaoTokenService.getValidAccessTokenOrRefresh(memberId))
            .thenThrow(new KakaoReauthenticationRequiredException());

        // when
        assertDoesNotThrow(() ->
            orderNotificationService.sendOrderCompleteNotification(memberId, orderId)
        );

        //then
        verify(kakaoTokenService).getValidAccessTokenOrRefresh(memberId);
        verifyNoInteractions(kakaoApiClient); // 토큰 얻기 실패하면 메시지는 전송되지 않음
    }

    @Test
    @DisplayName("KakaoApiClientException이 발생해도 예외를 던지지 않는다")
    void sendOrderCompleteNotification_clientException() {
        // given
        when(kakaoTokenService.getValidAccessTokenOrRefresh(memberId)).thenReturn(accessToken);
        doThrow(new KakaoApiClientException("카카오 API 오류"))
            .when(kakaoApiClient).sendKakaoMessage(anyString(), anyString());

        // when
        assertDoesNotThrow(() ->
            orderNotificationService.sendOrderCompleteNotification(memberId, orderId)
        );

        //then
        verify(kakaoTokenService).getValidAccessTokenOrRefresh(memberId);
        verify(kakaoApiClient).sendKakaoMessage(accessToken, "주문이 완료되었습니다. 주문번호: ORD-" + orderId);
    }

    @Test
    @DisplayName("KakaoApiServerException이 발생해도 예외를 던지지 않는다")
    void sendOrderCompleteNotification_serverException() {
        // given
        when(kakaoTokenService.getValidAccessTokenOrRefresh(memberId)).thenReturn(accessToken);
        doThrow(new KakaoApiServerException("카카오 서버 오류"))
            .when(kakaoApiClient).sendKakaoMessage(anyString(), anyString());

        // when
        assertDoesNotThrow(() ->
            orderNotificationService.sendOrderCompleteNotification(memberId, orderId)
        );

        //then
        verify(kakaoTokenService).getValidAccessTokenOrRefresh(memberId);
        verify(kakaoApiClient).sendKakaoMessage(accessToken, "주문이 완료되었습니다. 주문번호: ORD-" + orderId);
    }
}
