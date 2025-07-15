package gift.auth.dto;

import gift.auth.domain.TokenInfo;

public record LoginResponseDto(
    String tokenType,
    String accessToken,
    long expiresInSeconds,
    String refreshToken,
    long refreshTokenExpiresInSeconds
) {

    public static LoginResponseDto from(TokenInfo res) {
        return new LoginResponseDto(res.tokenType(), res.accessToken(), res.expiresIn(),
            res.refreshToken(), res.refreshTokenExpiresIn());
    }
}
