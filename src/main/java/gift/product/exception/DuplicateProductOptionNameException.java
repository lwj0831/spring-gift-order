package gift.product.exception;

import gift.global.exception.BusinessException;

public class DuplicateProductOptionNameException extends BusinessException {

    public DuplicateProductOptionNameException(String name) {
        super(ProductOptionErrorCode.DUPLICATE_PRODUCT_OPTION_NAME);
        addArgument("상품 옵션명", name);
    }

}
