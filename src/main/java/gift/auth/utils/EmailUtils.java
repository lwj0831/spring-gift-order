package gift.auth.utils;

public final class EmailUtils {
    private static final String KAKAO_EMAIL_FORMAT = "kakaoUser%d@kakao.com";

    public static String createEmailByKakaoId(Long kakaoId) {
        return String.format(KAKAO_EMAIL_FORMAT, kakaoId);
    }
}
