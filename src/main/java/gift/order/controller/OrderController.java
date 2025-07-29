package gift.order.controller;

import gift.auth.annotation.LoginUser;
import gift.infra.kakao.exception.OrderService;
import gift.order.dto.CreateOrderRequestDto;
import gift.order.dto.GetOrderResponseDto;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@LoginUser Long memberId,
        @RequestBody CreateOrderRequestDto dto) {
        Long orderId = orderService.createOrder(memberId, dto);
        return ResponseEntity.created(URI.create("/api/orders/" + orderId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetOrderResponseDto> getOrderInfo(@LoginUser Long memberId,
        @PathVariable(value = "id") Long orderId) {
        return ResponseEntity.ok(orderService.getOrderInfo(memberId, orderId));
    }

}
