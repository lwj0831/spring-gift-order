package gift.order.domain;

import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int orderQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prdouct_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOption productOption;

    protected OrderProduct() {
    }

    private OrderProduct(Long id, int orderQuantity, Order order, Product product,
        ProductOption productOption) {
        validateProduct(product);
        validateOption(productOption);
        validateOrderQuantity(orderQuantity);
        this.id = id;
        this.orderQuantity = orderQuantity;
        this.order = order;
        this.product = product;
        this.productOption = productOption;
    }

    public static OrderProduct of(Product product, ProductOption productOption, int orderQuantity) {
        return new OrderProduct(null, orderQuantity, null, product, productOption);
    }

    private void validateOrderQuantity(int orderQuantity) {
        if (orderQuantity <= 0) {
            throw new IllegalArgumentException("상품 수량은 0이하일 수 없습니다.");
        }
    }

    private void validateOption(ProductOption productOption) {
        if (productOption == null) {
            throw new IllegalArgumentException("상품 옵션은 상품이 null값일 수 없습니다.");
        }
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("상품 옵션은 상품이 null값일 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    private int getUnitPrice() {
        return product.getPrice();
    }

    public String getProductName() {
        return product.getName();
    }

    public String getProductOptionName() {
        return productOption.getName();
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public int getOrderPrice() {
        return getUnitPrice() * orderQuantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
