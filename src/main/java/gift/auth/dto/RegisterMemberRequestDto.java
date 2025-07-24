package gift.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterMemberRequestDto(
    @NotBlank(message = "회원명은 필수값입니다.")
    String username,
    @NotBlank(message = "이메일 값은 필수값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,
    @NotBlank(message = "비밀번호는 필수값입니다.")
    String password
) {

}
