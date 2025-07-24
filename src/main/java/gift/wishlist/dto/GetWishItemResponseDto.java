package gift.wishlist.dto;

import gift.product.domain.Product;
import gift.wishlist.domain.WishItem;

public record GetWishItemResponseDto(
    Long productId,
    String productName,
    int price,
    String imageUrl

) {

    public static GetWishItemResponseDto from(WishItem wishItem) {
        Product product = wishItem.getProduct();
        return new GetWishItemResponseDto(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl());
    }

}
