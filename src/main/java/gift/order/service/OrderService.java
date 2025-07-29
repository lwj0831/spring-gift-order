package gift.order.service;

import gift.auth.exception.ForbiddenException;
import gift.auth.exception.KakaoReauthenticationRequiredException;
import gift.auth.service.KakaoApiClient;
import gift.auth.service.KakaoTokenService;
import gift.member.domain.Member;
import gift.member.service.MemberService;
import gift.order.domain.Order;
import gift.order.domain.OrderProduct;
import gift.order.dto.CreateOrderRequestDto;
import gift.order.dto.GetOrderProductDto;
import gift.order.dto.GetOrderResponseDto;
import gift.order.exception.OrderErrorCode;
import gift.order.exception.OrderException;
import gift.order.repository.OrderJpaRepository;
import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.service.ProductOptionService;
import gift.product.service.ProductService;
import gift.wishlist.service.WishItemService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final ProductService productService;
    private final ProductOptionService productOptionService;
    private final MemberService memberSerivce;
    private final WishItemService wishItemService;
    private final KakaoTokenService kakaoTokenService;
    private final KakaoApiClient kakaoApiClient;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderJpaRepository orderJpaRepository, ProductService productService,
        ProductOptionService productOptionService, MemberService memberSerivce,
        WishItemService wishItemService, KakaoTokenService kakaoTokenService,
        KakaoApiClient kakaoApiClient) {
        this.orderJpaRepository = orderJpaRepository;
        this.productService = productService;
        this.productOptionService = productOptionService;
        this.memberSerivce = memberSerivce;
        this.wishItemService = wishItemService;
        this.kakaoTokenService = kakaoTokenService;
        this.kakaoApiClient = kakaoApiClient;
    }

    @Transactional
    public Long createOrder(Long memberId, CreateOrderRequestDto dto) {
        Member member = memberSerivce.findMemberOrThrow(memberId);

        List<OrderProduct> orderProducts = dto.orderProductList().stream()
            .map(d -> {
                Product product = productService.findProductOrThrow(d.productId());
                ProductOption option = productOptionService.findProductOptionOrThrow(
                    d.productOptionId());

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
        sendKakaoNotification(member, savedOrder.getId());

        return savedOrder.getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendKakaoNotification(Member member, Long orderId) {
        if (member.isKakaoUser()) {
            try {
                String kakaoAccessToken = kakaoTokenService.getValidAccessTokenOrRefresh(
                    member.getId());
                kakaoApiClient.sendKakaoMessage(kakaoAccessToken,
                    "주문이 완료되었습니다. 주문번호: ORD-" + orderId);
            } catch (KakaoReauthenticationRequiredException e) {
                logger.warn("카카오 재인증 필요로 메시지 전송 실패. 회원 ID: {}", member.getId());
            }
        }
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
