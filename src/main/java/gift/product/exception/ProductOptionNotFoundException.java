package gift.product.exception;

import gift.global.exception.BusinessException;

public class ProductOptionNotFoundException extends BusinessException {

    public ProductOptionNotFoundException(Long id) {
        super(ProductOptionErrorCode.PRODUCT_OPTION_NOT_FOUND);
        addArgument("상품 옵션 ID", id);
    }

}
