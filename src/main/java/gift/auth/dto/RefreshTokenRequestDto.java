package gift.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(
    @NotBlank(message = "리프레쉬 토큰값은 필수값입니다.")
    String refreshToken
) {

}
