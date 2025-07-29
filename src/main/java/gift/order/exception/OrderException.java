package gift.order.exception;

import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;

public class OrderException extends BusinessException {

    public OrderException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OrderException(ErrorCode errorCode, String key, String value) {
        super(errorCode);
        addArgument(key, value);
    }
}
