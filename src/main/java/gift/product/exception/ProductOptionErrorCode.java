package gift.product.exception;

import gift.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ProductOptionErrorCode implements ErrorCode {

    DUPLICATE_PRODUCT_OPTION_NAME(HttpStatus.BAD_REQUEST, "PRODUCT-004",
        "옵션명은 중복일 수 없습니다."),
    PRODUCT_OPTION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "PRODUCT-004",
        "해당 상품에 동일한 옵션명의 옵션이 이미 존재합니다."),
    PRODUCT_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-005", "상품 옵션이 존재하지 않습니다."),
    INVALID_PRODUCT_OPTION_NAME(HttpStatus.BAD_REQUEST, "PRODUCT-006", "유효하지 않은 상품 옵션명입니다.");

    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    ProductOptionErrorCode(HttpStatus status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
