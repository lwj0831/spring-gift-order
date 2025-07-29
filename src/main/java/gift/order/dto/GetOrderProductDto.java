package gift.order.dto;

import gift.order.domain.OrderProduct;

public record GetOrderProductDto (
    Long orderProductId,
    String productName,
    String optionName,
    int orderPrice,
    int orderQuantity

){

    public static GetOrderProductDto from(OrderProduct orderProduct){
        return new GetOrderProductDto(orderProduct.getId(),orderProduct.getProductName(),
            orderProduct.getProductOptionName(), orderProduct.getOrderPrice(),orderProduct.getOrderQuantity());
    }

}
