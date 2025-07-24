package gift.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    HttpStatus getStatus();

    String getErrorCode();

    String getErrorMessage();


}
