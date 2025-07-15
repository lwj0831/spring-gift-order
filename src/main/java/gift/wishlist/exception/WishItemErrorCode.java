package gift.wishlist.exception;

import gift.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum WishItemErrorCode implements ErrorCode {
    WISH_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "WISHITEM-001", "위시아이템이 존재하지 않습니다."),
    WISH_ITEM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "WISHITEM-002", "이미 위시리스트에 듳록된 상품입니다.");

    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    WishItemErrorCode(HttpStatus status, String errorCode, String errorMessage) {
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
