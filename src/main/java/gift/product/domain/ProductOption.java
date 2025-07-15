package gift.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.regex.Pattern;

@Entity
@Table(
    name = "product_option",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_product_option_product_id_name",
        columnNames = {"product_id", "name"}
    )
)
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private static final int QUANTITY_MIN_VALUE = 1;
    private static final int QUANTITY_MAX_VALUE = 100000000;

    private static final int NAME_MAX_LENGTH = 50;
    private static final String NAME_PATTERN = "^[\\p{L}\\p{N} ()\\[\\]\\+\\-\\&/_]{1,50}$";
    private static final Pattern NAME_REGEX = Pattern.compile(NAME_PATTERN);

    protected ProductOption() {
    }

    private ProductOption(Long id, String name, int quantity, Product product) {
        validateName(name);
        validateQuantity(quantity);

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        setProduct(product);
    }

    public static ProductOption of(String name, int quantity) {
        return new ProductOption(null, name, quantity, null);
    }

    public static ProductOption of(String name, int quantity, Product product) {
        return new ProductOption(null, name, quantity, product);
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void addQuantity(int quantity) {
        int result = this.quantity + quantity;
        if (result >= QUANTITY_MAX_VALUE) {
            throw new IllegalArgumentException("상품 수량 최대값(" + QUANTITY_MAX_VALUE + ") 초과입니다.");
        } else {
            this.quantity = result;
        }
    }

    public void subtractQuantity(int quantity) {
        int result = this.quantity - quantity;
        if (result < QUANTITY_MIN_VALUE) {
            throw new IllegalArgumentException("상품 수량이 부족합니다");
        } else {
            this.quantity = result;
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("옵션 이름은 필수입니다.");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("옵션 이름은 최대 " + NAME_MAX_LENGTH + "자까지 입력할 수 있습니다.");
        }
        if (!NAME_REGEX.matcher(name).matches()) {
            throw new IllegalArgumentException("옵션 이름에 허용되지 않은 특수 문자가 포함되어 있습니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < QUANTITY_MIN_VALUE || quantity >= QUANTITY_MAX_VALUE) {
            throw new IllegalArgumentException(
                "옵션 수량은 " + QUANTITY_MIN_VALUE + " 이상, " + QUANTITY_MAX_VALUE + " 미만이어야 합니다.");
        }
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
