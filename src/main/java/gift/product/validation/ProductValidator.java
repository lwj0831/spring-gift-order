package gift.product.validation;

import gift.product.exception.InvalidProductNameException;
import gift.product.exception.InvalidProductSortFieldException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    public void validateProductName(String productName) {
        if (InvalidProductWords.contains(productName)) {
            List<String> foundWords = InvalidProductWords.findMatches(productName);
            throw new InvalidProductNameException(
                "상품명에 다음 키워드를 포함할 수 없습니다: " + String.join(", ", foundWords)
            );
        }
    }

    public void validateProductSortField(String sortField) {
        if (!ProductSortField.isValid(sortField)) {
            throw new InvalidProductSortFieldException();
        }
    }

    public void validateProductSortFields(List<String> sortFields) {
        List<String> invalidFields = sortFields.stream()
            .filter(field -> !ProductSortField.isValid(field))
            .toList();

        if (!invalidFields.isEmpty()) {
            throw new InvalidProductSortFieldException(
                "다음 정렬 필드는 허용되지 않습니다: " + String.join(", ", invalidFields)
            );
        }
    }

}
