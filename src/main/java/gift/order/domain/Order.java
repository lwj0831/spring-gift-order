package gift.order.domain;

import gift.global.common.jpa.TimeBaseEntity;
import gift.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Order() {
    }

    private Order(Long id, List<OrderProduct> orderProducts, Member member) {
        validateOrderProductAtLeastOne(orderProducts);

        this.id = id;
        orderProducts.forEach(this::addOrderProduct);
        this.member = member;
    }

    public static Order of(List<OrderProduct> orderProducts, Member member) {
        return new Order(null, orderProducts, member);
    }

    private void validateOrderProductAtLeastOne(List<OrderProduct> orderProducts) {
        if (orderProducts == null || orderProducts.isEmpty()) {
            throw new IllegalArgumentException("주문 상품은 null이거나 최소 1개 이상이어야 합니다.");
        }
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
        this.totalPrice += orderProduct.getOrderPrice();
    }

    private int calculateTotalPrice() {
        return orderProducts.stream()
            .mapToInt(OrderProduct::getOrderPrice).sum();
    }

    public Long getId() {
        return id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public Member getMember() {
        return member;
    }
}
