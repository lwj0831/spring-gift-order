package gift.product.exception;


import gift.global.exception.BusinessException;

public class InvalidProductSortFieldException extends BusinessException {

    public InvalidProductSortFieldException() {
        super(ProductErrorCode.INVALID_SORT_FIELD_ERROR);
    }

    public InvalidProductSortFieldException(String message) {
        super(ProductErrorCode.INVALID_SORT_FIELD_ERROR, message);
    }
}
