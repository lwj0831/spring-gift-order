package gift.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.auth.exception.ForbiddenException;
import gift.member.domain.Member;
import gift.member.domain.MemberType;
import gift.member.service.MemberService;
import gift.order.domain.Order;
import gift.order.domain.OrderProduct;
import gift.order.dto.CreateOrderProductDto;
import gift.order.dto.CreateOrderRequestDto;
import gift.order.event.OrderCompletedEvent;
import gift.order.repository.OrderJpaRepository;
import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.service.ProductOptionService;
import gift.product.service.ProductService;
import gift.wishlist.domain.WishItem;
import gift.wishlist.service.WishItemService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderJpaRepository orderJpaRepository;
    @Mock
    private ProductService productService;
    @Mock
    private ProductOptionService productOptionService;
    @Mock
    private MemberService memberService;
    @Mock
    private WishItemService wishItemService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private OrderService orderService;

    private final Long memberId = 1L;
    private final Long orderId = 10L;

    private Product product;
    private ProductOption option;
    private Member member;
    private CreateOrderRequestDto dto;

    @BeforeEach
    void setUp() {
        option = ProductOption.of("옵션", 10, product);
        product = Product.of("상품", 1000, "설명", "image", List.of(option));
        member = Member.of("테스트회원", MemberType.GENERAL);

        CreateOrderProductDto orderProductDto = new CreateOrderProductDto(
            1L, 1L, 1
        );
        dto = new CreateOrderRequestDto(List.of(orderProductDto));
    }

    @Test
    @DisplayName("일반 회원 주문 생성 시 저장되고 이벤트는 발행되지 않는다")
    void createOrder_normalMember() {
        //given
        WishItem wishItem = WishItem.of(member, product);
        member.getWishItems().add(wishItem);

        //when
        when(memberService.findMemberOrThrow(memberId)).thenReturn(member);
        when(productService.findProductOrThrow(any())).thenReturn(product);
        when(productOptionService.findProductOptionOrThrow(any())).thenReturn(option);
        when(wishItemService.checkWishItemExists(wishItem.getId())).thenReturn(true);
        when(orderJpaRepository.save(any())).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            ReflectionTestUtils.setField(order, "id", orderId);
            return order;
        });

        Long result = orderService.createOrder(memberId, dto);

        //then
        assertEquals(orderId, result);
        verify(wishItemService).deleteWishItem(eq(memberId), eq(wishItem.getId()));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("카카오 회원 주문 시 이벤트가 발행된다")
    void createOrder_kakaoMember() {
        //given
        member = Member.of("카카오", MemberType.KAKAO);

        //when
        when(memberService.findMemberOrThrow(memberId)).thenReturn(member);
        when(productService.findProductOrThrow(any())).thenReturn(product);
        when(productOptionService.findProductOptionOrThrow(any())).thenReturn(option);
        when(orderJpaRepository.save(any())).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            ReflectionTestUtils.setField(order, "id", orderId);
            return order;
        });

        orderService.createOrder(memberId, dto);

        //then
        verify(eventPublisher).publishEvent(any(OrderCompletedEvent.class));
    }

    @Test
    @DisplayName("다른 회원의 주문 정보를 조회하면 ForbiddenException 발생")
    void getOrderInfo_forbidden() {
        //given
        Member anotherMember = Member.of("다른유저", MemberType.GENERAL);
        Order order = Order.of(List.of(OrderProduct.of(product, option, 1)), anotherMember);
        ReflectionTestUtils.setField(order, "id", orderId);

        //when
        when(orderJpaRepository.findOrderInfoById(orderId)).thenReturn(Optional.of(order));

        //then
        assertThrows(ForbiddenException.class, () -> orderService.getOrderInfo(memberId, orderId));
    }

}

