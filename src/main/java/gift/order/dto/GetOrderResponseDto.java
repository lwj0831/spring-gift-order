package gift.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GetOrderResponseDto(

    LocalDateTime orderDate,
    int totalPrice,
    List<GetOrderProductDto> orderProductList

) {

}
