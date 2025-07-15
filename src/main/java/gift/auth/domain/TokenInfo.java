package gift.auth.domain;

public record TokenInfo(
    String tokenType,
    String accessToken,
    long expiresIn,
    String refreshToken,
    long refreshTokenExpiresIn
) {

}
