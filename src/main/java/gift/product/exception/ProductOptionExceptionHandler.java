package gift.product.exception;

import gift.global.exception.ErrorResponseFactory;
import gift.global.exception.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductOptionExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProductExceptionHandler.class);

    @ExceptionHandler(DuplicateProductOptionNameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateOptionNameException(
        DuplicateProductOptionNameException exception) {
        logger.error("DuplicateOptionNameException occurs: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(ProductOptionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOptionNotFoundException(
        ProductOptionNotFoundException exception) {
        logger.error("OptionNotFoundException occurs: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(InvalidProductOptionNameException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProductOptionNameException(
        InvalidProductOptionNameException exception) {
        logger.error("InvalidProductOptionNameException occurs: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

}
