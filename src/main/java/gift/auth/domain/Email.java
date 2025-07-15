package gift.auth.domain;

import java.util.regex.Pattern;
import org.springframework.util.Assert;

public class Email {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private final String emailText;

    public static Email createEmail(String email) {
        Assert.hasText(email, "이메일은 null이거나 빈 값일 수 없습니다.");

        if (!validateEmail(email)) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        return new Email(email);
    }

    private static boolean validateEmail(String email) {
        return pattern.matcher(email).matches();
    }

    private Email(String email) {
        this.emailText = email;
    }

    public String getEmailText() {
        return emailText;
    }
}
