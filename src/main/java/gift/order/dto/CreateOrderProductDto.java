package gift.order.dto;

public record CreateOrderProductDto(
    Long productId,
    Long productOptionId,
    int orderQuantity
) {

}
