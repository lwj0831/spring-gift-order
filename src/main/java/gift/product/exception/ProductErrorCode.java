package gift.product.exception;

import gift.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ProductErrorCode implements ErrorCode {
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-001", "상품 정보를 찾을 수 없습니다"),
    INVALID_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "PRODUCT-002", "상품명에 포함할 수 없는 키워드가 존재합니다"),
    INVALID_SORT_FIELD_ERROR(HttpStatus.BAD_REQUEST, "PRODUCT-003", "상품 페이징 정렬 필드 값이 올바르지 않습니다");
    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    ProductErrorCode(HttpStatus status, String errorCode, String errorMessage) {
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
