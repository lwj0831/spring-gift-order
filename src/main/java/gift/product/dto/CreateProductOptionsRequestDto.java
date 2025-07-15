package gift.product.dto;

import jakarta.validation.Valid;
import java.util.List;

public record CreateProductOptionsRequestDto(
    @Valid
    List<CreateProductOptionDto> optionRequestDtoList
) {

}
