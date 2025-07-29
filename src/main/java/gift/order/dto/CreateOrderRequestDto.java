package gift.order.dto;

import java.util.List;

public record CreateOrderRequestDto(
    List<CreateOrderProductDto> orderProductList
) {

}
