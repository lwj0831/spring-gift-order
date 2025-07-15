package gift.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ProductOption 도메인 테스트")
class ProductOptionTest {

    @Nested
    @DisplayName("ProductOption 생성 테스트")
    class CreateProductOptionTest {

        @Test
        @DisplayName("정상적인 값으로 ProductOption을 생성할 수 있다")
        void createProductOptionWithValidValues() {
            // given
            String name = "옵션1";
            int quantity = 10;

            // when
            ProductOption productOption = ProductOption.of(name, quantity);

            // then
            assertThat(productOption.getName()).isEqualTo(name);
            assertThat(productOption.getQuantity()).isEqualTo(quantity);
            assertThat(productOption.getId()).isNull();
            assertThat(productOption.getProduct()).isNull();
        }

        @Test
        @DisplayName("Product와 함께 ProductOption을 생성할 수 있다")
        void createProductOptionWithProduct() {
            // given
            String name = "옵션1";
            int quantity = 10;
            Product product = mock(Product.class);

            // when
            ProductOption productOption = ProductOption.of(name, quantity, product);

            // then
            assertThat(productOption.getName()).isEqualTo(name);
            assertThat(productOption.getQuantity()).isEqualTo(quantity);
            assertThat(productOption.getProduct()).isEqualTo(product);
        }

        @Test
        @DisplayName("영문, 숫자, 특수문자가 포함된 이름으로 생성할 수 있다")
        void createProductOptionWithSpecialCharacters() {
            // given
            String name = "Option-1 (Large) [Blue] Size/Weight & More";

            // when
            ProductOption productOption = ProductOption.of(name, 10);

            // then
            assertThat(productOption.getName()).isEqualTo(name);
        }
    }

    @Nested
    @DisplayName("옵션 이름 검증 테스트")
    class ValidateNameTest {

        @Test
        @DisplayName("null 이름으로 생성 시 예외가 발생한다")
        void throwExceptionWhenNameIsNull() {
            // when & then
            assertThatThrownBy(() -> ProductOption.of(null, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("옵션 이름은 필수입니다.");
        }

        @Test
        @DisplayName("빈 문자열 이름으로 생성 시 예외가 발생한다")
        void throwExceptionWhenNameIsEmpty() {
            // when & then
            assertThatThrownBy(() -> ProductOption.of("", 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("옵션 이름은 필수입니다.");
        }

        @Test
        @DisplayName("공백만 포함된 이름으로 생성 시 예외가 발생한다")
        void throwExceptionWhenNameIsBlank() {
            // when & then
            assertThatThrownBy(() -> ProductOption.of("   ", 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("옵션 이름은 필수입니다.");
        }

        @Test
        @DisplayName("50자를 초과하는 이름으로 생성 시 예외가 발생한다")
        void throwExceptionWhenNameExceedsMaxLength() {
            // given
            String longName = "a".repeat(51);

            // when & then
            assertThatThrownBy(() -> ProductOption.of(longName, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("옵션 이름은 최대 50자까지 입력할 수 있습니다.");
        }

        @Test
        @DisplayName("50자 이름으로 생성할 수 있다")
        void createProductOptionWithMaxLengthName() {
            // given
            String maxLengthName = "a".repeat(50);

            // when
            ProductOption productOption = ProductOption.of(maxLengthName, 10);

            // then
            assertThat(productOption.getName()).isEqualTo(maxLengthName);
        }

        @ParameterizedTest
        @ValueSource(strings = {"@", "#", "$", "%", "^", "*", "=", "!", "?", "<", ">", ",", ".",
            ":", ";", "\"", "'", "|", "\\", "~", "`"})
        @DisplayName("허용되지 않은 특수문자가 포함된 이름으로 생성 시 예외가 발생한다")
        void throwExceptionWhenNameContainsInvalidSpecialCharacters(String invalidChar) {
            // given
            String nameWithInvalidChar = "옵션" + invalidChar;

            // when & then
            assertThatThrownBy(() -> ProductOption.of(nameWithInvalidChar, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("옵션 이름에 허용되지 않은 특수 문자가 포함되어 있습니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"옵션 1", "Option-A", "사이즈(Large)", "색상[Blue]", "무게&크기",
            "타입/종류", "언더_바"})
        @DisplayName("허용된 특수문자가 포함된 이름으로 생성할 수 있다")
        void createProductOptionWithValidSpecialCharacters(String validName) {
            // when
            ProductOption productOption = ProductOption.of(validName, 10);

            // then
            assertThat(productOption.getName()).isEqualTo(validName);
        }
    }

    @Nested
    @DisplayName("옵션 수량 검증 테스트")
    class ValidateQuantityTest {

        @Test
        @DisplayName("최소값(1) 미만의 수량으로 생성 시 예외가 발생한다")
        void throwExceptionWhenQuantityIsBelowMinimum() {
            // when & then
            assertThatThrownBy(() -> ProductOption.of("옵션1", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("옵션 수량은 1 이상, 100000000 미만이어야 합니다.");
        }

        @Test
        @DisplayName("최대값(100000000) 이상의 수량으로 생성 시 예외가 발생한다")
        void throwExceptionWhenQuantityIsAboveMaximum() {
            // when & then
            assertThatThrownBy(() -> ProductOption.of("옵션1", 100000000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("옵션 수량은 1 이상, 100000000 미만이어야 합니다.");
        }

        @Test
        @DisplayName("최소값(1)으로 생성할 수 있다")
        void createProductOptionWithMinimumQuantity() {
            // when
            ProductOption productOption = ProductOption.of("옵션1", 1);

            // then
            assertThat(productOption.getQuantity()).isEqualTo(1);
        }

        @Test
        @DisplayName("최대값-1(99999999)으로 생성할 수 있다")
        void createProductOptionWithMaximumQuantity() {
            // when
            ProductOption productOption = ProductOption.of("옵션1", 99999999);

            // then
            assertThat(productOption.getQuantity()).isEqualTo(99999999);
        }
    }

    @Nested
    @DisplayName("수량 추가 테스트")
    class AddQuantityTest {

        @Test
        @DisplayName("정상적으로 수량을 추가할 수 있다")
        void addQuantitySuccessfully() {
            // given
            ProductOption productOption = ProductOption.of("옵션1", 10);

            // when
            productOption.addQuantity(5);

            // then
            assertThat(productOption.getQuantity()).isEqualTo(15);
        }

        @Test
        @DisplayName("수량 추가 후 최대값을 초과하면 예외가 발생한다")
        void throwExceptionWhenAddQuantityExceedsMaximum() {
            // given
            ProductOption productOption = ProductOption.of("옵션1", 99999999);

            // when & then
            assertThatThrownBy(() -> productOption.addQuantity(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 수량 최대값(100000000) 초과입니다.");
        }

        @Test
        @DisplayName("수량 추가 후 정확히 최대값-1이 되면 성공한다")
        void addQuantityToReachMaximum() {
            // given
            ProductOption productOption = ProductOption.of("옵션1", 99999998);

            // when
            productOption.addQuantity(1);

            // then
            assertThat(productOption.getQuantity()).isEqualTo(99999999);
        }
    }

    @Nested
    @DisplayName("수량 차감 테스트")
    class SubtractQuantityTest {

        @Test
        @DisplayName("정상적으로 수량을 차감할 수 있다")
        void subtractQuantitySuccessfully() {
            // given
            ProductOption productOption = ProductOption.of("옵션1", 10);

            // when
            productOption.subtractQuantity(3);

            // then
            assertThat(productOption.getQuantity()).isEqualTo(7);
        }

        @Test
        @DisplayName("수량 차감 후 최소값 미만이 되면 예외가 발생한다")
        void throwExceptionWhenSubtractQuantityBelowMinimum() {
            // given
            ProductOption productOption = ProductOption.of("옵션1", 5);

            // when & then
            assertThatThrownBy(() -> productOption.subtractQuantity(6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 수량이 부족합니다");
        }

        @Test
        @DisplayName("수량 차감 후 정확히 최소값(1)이 되면 성공한다")
        void subtractQuantityToReachMinimum() {
            // given
            ProductOption productOption = ProductOption.of("옵션1", 5);

            // when
            productOption.subtractQuantity(4);

            // then
            assertThat(productOption.getQuantity()).isEqualTo(1);
        }

        @Test
        @DisplayName("전체 수량을 차감하려고 하면 예외가 발생한다")
        void throwExceptionWhenSubtractingAllQuantity() {
            // given
            ProductOption productOption = ProductOption.of("옵션1", 5);

            // when & then
            assertThatThrownBy(() -> productOption.subtractQuantity(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 수량이 부족합니다");
        }
    }
}