package gift.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.dto.CreateProductOptionDto;
import gift.product.exception.DuplicateProductOptionNameException;
import gift.product.exception.ProductNotFoundException;
import gift.product.exception.ProductOptionNotFoundException;
import gift.product.repository.ProductOptionJpaRepository;
import gift.product.validation.ProductOptionValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductOptionServiceTest {

    @Mock
    private ProductOptionJpaRepository productOptionRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ProductOptionValidator productOptionValidator;

    @InjectMocks
    private ProductOptionService productOptionService;

    private Product mockProduct;
    private ProductOption mockProductOption;

    @BeforeEach
    void setUp() {
        mockProduct = mock(Product.class);
        mockProductOption = mock(ProductOption.class);
    }

    @Test
    @DisplayName("상품 옵션 등록 성공")
    void registerProductOption_Success() {
        // given
        Long productId = 1L;
        CreateProductOptionDto dto = new CreateProductOptionDto("옵션명", 100);

        when(productService.findProductOrThrow(productId)).thenReturn(mockProduct);
        when(productOptionRepository.save(any(ProductOption.class))).thenReturn(mockProductOption);
        when(mockProductOption.getId()).thenReturn(1L);

        // when
        Long result = productOptionService.registerProductOption(productId, dto);

        // then
        assertThat(result).isEqualTo(1L);
        verify(productService).findProductOrThrow(productId);
        verify(productOptionValidator).validateOptionNameUniqueness(productId, List.of(dto.name()));
        verify(productOptionRepository).save(any(ProductOption.class));
    }

    @Test
    @DisplayName("존재하지 않는 상품에 옵션 등록 시 예외 발생")
    void registerProductOption_ProductNotFound() {
        // given
        Long productId = 999L;
        CreateProductOptionDto dto = new CreateProductOptionDto("옵션명", 100);

        when(productService.findProductOrThrow(productId))
            .thenThrow(new ProductNotFoundException(productId));

        // when & then
        assertThatThrownBy(() -> productOptionService.registerProductOption(productId, dto))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("중복된 옵션명으로 등록 시 예외 발생")
    void registerProductOption_DuplicateOptionName() {
        // given
        Long productId = 1L;
        CreateProductOptionDto dto = new CreateProductOptionDto("중복옵션", 100);

        when(productService.findProductOrThrow(productId)).thenReturn(mockProduct);
        doThrow(new DuplicateProductOptionNameException("중복옵션"))
            .when(productOptionValidator)
            .validateOptionNameUniqueness(productId, List.of(dto.name()));

        // when & then
        assertThatThrownBy(() -> productOptionService.registerProductOption(productId, dto))
            .isInstanceOf(DuplicateProductOptionNameException.class);
    }

    @Test
    @DisplayName("옵션 수량 감소 성공")
    void addProductOptionQuantity_Success() {
        // given
        Long optionId = 1L;
        int quantity = 50;

        when(productOptionRepository.findById(optionId)).thenReturn(Optional.of(mockProductOption));

        // when
        productOptionService.subtractProductOptionQuantity(optionId, quantity);

        // then
        verify(mockProductOption).subtractQuantity(quantity);
    }

    @Test
    @DisplayName("존재하지 않는 옵션 수량 감소 시 예외 발생")
    void addProductOptionQuantity_OptionNotFound() {
        // given
        Long optionId = 999L;
        int quantity = 50;

        when(productOptionRepository.findById(optionId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(
            () -> productOptionService.subtractProductOptionQuantity(optionId, quantity))
            .isInstanceOf(ProductOptionNotFoundException.class);
    }

    @Test
    @DisplayName("옵션 삭제 성공")
    void deleteProductOption_Success() {
        // given
        Long optionId = 1L;

        when(productOptionRepository.findById(optionId)).thenReturn(Optional.of(mockProductOption));

        // when
        productOptionService.deleteProductOption(optionId);

        // then
        verify(productOptionRepository).delete(mockProductOption);
    }
}