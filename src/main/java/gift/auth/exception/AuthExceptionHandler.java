package gift.auth.exception;

import gift.global.exception.ErrorResponseFactory;
import gift.global.exception.dto.ErrorResponse;
import gift.product.exception.ProductExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProductExceptionHandler.class);

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(
        InvalidTokenException exception) {
        logger.error("Invalid token exception: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorResponse> handleExpiredTokenException(
        ExpiredTokenException exception) {
        logger.error("Expired token exception: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedEmailException(
        DuplicatedEmailException exception) {
        logger.error("Duplicated email exception: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatchException(
        PasswordMismatchException exception) {
        logger.error("Password mismatch exception: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(
        ForbiddenException exception) {
        logger.error("Forbidden exception: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoginException(
        InvalidLoginException exception) {
        logger.error("Invalid login exception: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(KakaoApiClientException.class)
    public ResponseEntity<ErrorResponse> handleKakaoApiClientException(
        KakaoApiClientException exception) {
        logger.error("KakaoApiClient exception: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

    @ExceptionHandler(KakaoApiServerException.class)
    public ResponseEntity<ErrorResponse> handleKakaoApiServerException(
        KakaoApiServerException exception) {
        logger.error("KakaoApiServer exception: {}", exception.getMessage());

        return ErrorResponseFactory.createErrorResponse(exception);
    }

}
