package gift.global.common.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PageRequestDto(
    @PositiveOrZero(message = "page offset값은 음수일 수 없습니다.")
    int offset,
    @Positive(message = "page size값은 0이거나 음수일 수 없습니다")
    int pageSize,
    @Valid
    @NotNull
    SortInfo sortInfo
) {

    public Pageable toPageable() {
        return PageRequest.of(offset, pageSize,
            Sort.by(sortInfo.sortDirection(), sortInfo.sortField()));
    }

}
