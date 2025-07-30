package gift.order.service;

import gift.auth.exception.ForbiddenException;
import gift.member.domain.Member;
import gift.member.service.MemberService;
import gift.order.domain.Order;
import gift.order.domain.OrderProduct;
import gift.order.dto.CreateOrderRequestDto;
import gift.order.dto.GetOrderProductDto;
import gift.order.dto.GetOrderResponseDto;
import gift.order.event.OrderCompletedEvent;
import gift.order.exception.OrderErrorCode;
import gift.order.exception.OrderException;
import gift.order.repository.OrderJpaRepository;
import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.service.ProductOptionService;
import gift.product.service.ProductService;
import gift.wishlist.service.WishItemService;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final ProductService productService;
    private final ProductOptionService productOptionService;
    private final MemberService memberSerivce;
    private final WishItemService wishItemService;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderJpaRepository orderJpaRepository, ProductService productService,
        ProductOptionService productOptionService, MemberService memberSerivce,
        WishItemService wishItemService, ApplicationEventPublisher eventPublisher) {
        this.orderJpaRepository = orderJpaRepository;
        this.productService = productService;
        this.productOptionService = productOptionService;
        this.memberSerivce = memberSerivce;
        this.wishItemService = wishItemService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Long createOrder(Long memberId, CreateOrderRequestDto dto) {
        Member member = memberSerivce.findMemberOrThrow(memberId);

        List<OrderProduct> orderProducts = dto.orderProductList().stream()
            .map(d -> {
                Product product = productService.findProductOrThrow(d.productId());
                ProductOption option = productOptionService.findProductOptionOrThrow(
                    d.productOptionId());

                //옵션 수량 차감
                option.subtractQuantity(d.orderQuantity());

                //위시 아이템 삭제
                member.getWishItems().forEach(w ->
                {
                    if (w.getProduct().equals(product) && wishItemService.checkWishItemExists(
                        w.getId())) {
                        wishItemService.deleteWishItem(memberId, w.getId());
                    }
                });

                return OrderProduct.of(product, option, d.orderQuantity());
            })
            .toList();

        Order savedOrder = orderJpaRepository.save(Order.of(orderProducts, member));

        //카카오 소셜 회원인 경우 메세지 보내기 로직 추가
        if (member.isKakaoUser()) {
            eventPublisher.publishEvent(new OrderCompletedEvent(memberId, savedOrder.getId()));
        }

        return savedOrder.getId();
    }

    @Transactional(readOnly = true)
    public GetOrderResponseDto getOrderInfo(Long memberId, Long orderId) {
        Order order = findOrderInfoOrThrow(orderId);

        //리소스 소유권 검증
        if (!memberId.equals(order.getMember().getId())) {
            throw new ForbiddenException();
        }

        List<GetOrderProductDto> orderProductDtoList = order.getOrderProducts().stream()
            .map(GetOrderProductDto::from).toList();

        return new GetOrderResponseDto(order.getUpdateDate(), order.getTotalPrice(),
            orderProductDtoList);
    }

    public Order findOrderOrThrow(Long orderId) {
        return orderJpaRepository.findById(orderId)
            .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND, "orderId",
                orderId.toString()));
    }

    public Order findOrderInfoOrThrow(Long orderId) {
        return orderJpaRepository.findOrderInfoById(orderId)
            .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND, "orderId",
                orderId.toString()));
    }


}
