package gift.global.common.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponseDto<T>(
    List<T> content,
    int page,
    int size,
    boolean hasNext,
    boolean hasPrevious
) {

    public static <T> PageResponseDto<T> from(Page<T> page) {
        return new PageResponseDto<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            !page.isLast(),
            !page.isFirst()
        );
    }

}
