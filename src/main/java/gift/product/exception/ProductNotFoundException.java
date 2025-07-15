package gift.product.exception;

import gift.global.exception.BusinessException;

public class ProductNotFoundException extends BusinessException {


    public ProductNotFoundException(Long id) {
        super(ProductErrorCode.PRODUCT_NOT_FOUND);
        addArgument("상품 ID", id);
    }

}
