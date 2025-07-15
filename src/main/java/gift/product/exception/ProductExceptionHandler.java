package gift.product.exception;

import gift.global.exception.ErrorResponseFactory;
import gift.global.exception.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProductExceptionHandler.class);

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
        ProductNotFoundException exception) {
        logger.error("Product not found: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(InvalidProductNameException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProductNameException(
        InvalidProductNameException exception) {
        logger.error("Invalid product name: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(InvalidProductSortFieldException.class)
    public ResponseEntity<ErrorResponse> handleSortFieldException(
        InvalidProductSortFieldException exception) {

        return ErrorResponseFactory.createErrorResponse(exception);
    }
}