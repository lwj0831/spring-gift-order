package gift.wishlist.dto;

public record SimpleWishItemDto(
    Long productId,
    String name,
    int price,
    String imageUrl
) {

}
