package gift.product.exception;

import gift.global.exception.BusinessException;

public class ProductOptionNameAlreadyExistsException extends BusinessException {

    public ProductOptionNameAlreadyExistsException(String name) {
        super(ProductOptionErrorCode.DUPLICATE_PRODUCT_OPTION_NAME);
        addArgument("상품 옵션명", name);
    }
}
