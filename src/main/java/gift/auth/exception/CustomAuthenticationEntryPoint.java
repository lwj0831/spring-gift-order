package gift.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(
        CustomAuthenticationEntryPoint.class);

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        logger.error("AuthenticationException occurs: {}", authException.getMessage(),
            authException);
        ErrorResponse errorResponse = determineErrorResponse(authException);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

    private ErrorResponse determineErrorResponse(AuthenticationException authException) {
        if (authException instanceof BadCredentialsException) {
            return ErrorResponse.from(AuthErrorCode.UNAUTHORIZED);
        } else if (authException instanceof InsufficientAuthenticationException) {
            return ErrorResponse.from(AuthErrorCode.UNAUTHORIZED);
        } else if (authException.getMessage().contains("expired")) {
            return ErrorResponse.from(AuthErrorCode.EXPIRED_TOKEN);
        } else if (authException.getMessage().contains("invalid")) {
            return ErrorResponse.from(AuthErrorCode.INVALID_TOKEN);
        }
        return ErrorResponse.from(AuthErrorCode.UNAUTHORIZED);
    }
}
