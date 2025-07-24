package gift.auth.dto;

import gift.auth.domain.TokenInfo;

public record RegisterMemberResponseDto(
    String tokenType,
    String accessToken,
    long expiresInSeconds,
    String refreshToken,
    long refreshTokenExpiresInSeconds,
    Long memberId
) {

    public static RegisterMemberResponseDto from(TokenInfo res, Long memberId) {
        return new RegisterMemberResponseDto(res.tokenType(), res.accessToken(), res.expiresIn(),
            res.refreshToken(), res.refreshTokenExpiresIn(), memberId);
    }

}
