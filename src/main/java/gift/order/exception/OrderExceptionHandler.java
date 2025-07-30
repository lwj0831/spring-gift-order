package gift.order.exception;

import gift.global.exception.ErrorResponseFactory;
import gift.global.exception.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(OrderExceptionHandler.class);

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(OrderException e) {
        logger.error("OrderException occur: {}", e.getErrorCode(), e);

        return ErrorResponseFactory.createErrorResponse(e);
    }

}
