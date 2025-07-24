package gift.product.dto;

import gift.product.domain.ProductOption;

public record GetProductOptionResponseDto(
    Long id,
    String name,
    int quantity
) {

    public static GetProductOptionResponseDto from(ProductOption productOption) {
        return new GetProductOptionResponseDto(productOption.getId(), productOption.getName(),
            productOption.getQuantity());
    }
}
