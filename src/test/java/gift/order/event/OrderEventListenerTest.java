package gift.order.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import gift.order.service.OrderNotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderEventListenerTest {

    @Mock
    private OrderNotificationService orderNotificationService;

    @InjectMocks
    private OrderEventListener orderEventListener;

    @Test
    @DisplayName("주문 완료 이벤트를 정상적으로 처리한다")
    void handleOrderCompletedAfterCommit_success() {
        // given
        Long memberId = 1L;
        Long orderId = 100L;
        OrderCompletedEvent event = new OrderCompletedEvent(memberId, orderId);

        // when & then
        assertDoesNotThrow(() -> orderEventListener.handleOrderCompletedAfterCommit(event));
        verify(orderNotificationService, times(1))
            .sendOrderCompleteNotification(memberId, orderId);
    }

}