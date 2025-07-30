package gift.infra.kakao.dto;

public record KakaoUserInfoResponseDto(
    Long id,
    Properties properties
) {

    public record Properties(
        String nickname
    ) {

    }
}
