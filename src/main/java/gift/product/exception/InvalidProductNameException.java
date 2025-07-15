package gift.product.exception;

import gift.global.exception.BusinessException;

public class InvalidProductNameException extends BusinessException {

    public InvalidProductNameException() {
        super(ProductErrorCode.INVALID_PRODUCT_NAME);
    }

    public InvalidProductNameException(String message) {
        super(ProductErrorCode.INVALID_PRODUCT_NAME, message);
    }
}
