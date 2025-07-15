package gift.product.dto;

import jakarta.validation.constraints.NotNull;

public record CreateProductOptionDto(

    @NotNull
    String name,

    @NotNull
    int quantity
) {

}
