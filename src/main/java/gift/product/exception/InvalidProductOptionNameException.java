package gift.product.exception;

import gift.global.exception.BusinessException;

public class InvalidProductOptionNameException extends BusinessException {


    public InvalidProductOptionNameException(String name) {
        super(ProductOptionErrorCode.INVALID_PRODUCT_OPTION_NAME);
        addArgument("상품 옵션명", name);
    }

}
