package gift.wishlist.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterWishItemRequestDto(
    @NotNull(message = "productId는 null일 수 없습니다.")
    Long productId
) {

}
