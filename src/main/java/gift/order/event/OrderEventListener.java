package gift.order.event;

import gift.order.service.OrderNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventListener {

    private final OrderNotificationService orderNotificationService;
    private final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    public OrderEventListener(OrderNotificationService orderNotificationService) {
        this.orderNotificationService = orderNotificationService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderCompletedAfterCommit(OrderCompletedEvent event) {
        logger.info("트랜잭션 커밋 후 주문 완료 이벤트 처리. 사용자: {}, 주문: {}",
            event.getMemberId(), event.getOrderId());

        orderNotificationService.sendOrderCompleteNotification(
            event.getMemberId(),
            event.getOrderId()
        );
    }

}
