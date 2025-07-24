package gift.product.validation;

import gift.product.exception.InvalidProductSortFieldException;
import java.util.Arrays;

public enum ProductSortField {
    ID("id"),
    NAME("name"),
    PRICE("price");

    private final String fieldName;

    ProductSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static ProductSortField from(String fieldName) {
        return Arrays.stream(ProductSortField.values())
            .filter(field -> field.fieldName.equalsIgnoreCase(fieldName))
            .findFirst()
            .orElseThrow(InvalidProductSortFieldException::new);
    }

    public static boolean isValid(String fieldName) {
        return Arrays.stream(values())
            .map(ProductSortField::getFieldName)
            .anyMatch(fieldName::equalsIgnoreCase);
    }

}
