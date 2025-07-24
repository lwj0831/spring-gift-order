package gift.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
    @NotBlank(message = "이메일은 필수값입니다.")
    @Email
    String email,
    String password
) {

}
