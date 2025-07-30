package gift.order.event;

import java.time.LocalDateTime;

public class OrderCompletedEvent {

    private final Long memberId;
    private final Long orderId;
    private final LocalDateTime occurredAt;

    public OrderCompletedEvent(Long memberId, Long orderId) {
        this.memberId = memberId;
        this.orderId = orderId;
        this.occurredAt = LocalDateTime.now();
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
