package gift.global.common.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Sort.Direction;

public record SortInfo(

    @NotBlank(message = "정렬 필드값은 빈 값일 수 없습니다")
    String sortField,
    Direction sortDirection
) {

}