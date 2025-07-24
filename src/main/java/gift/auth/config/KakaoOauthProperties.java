package gift.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoOauthProperties(
    String clientId,
    String clientSecret,
    String redirectUri,
    Urls urls
) {

    public record Urls(
        String baseAuth,
        String baseApi,
        String token,
        String authorize,
        String userInfo,
        String sendMessage,
        String unlink
    ) {

        public String getTokenUrl() {
            return baseAuth + token;
        }

        public String getAuthorizeUrl() {
            return baseAuth + authorize;
        }

        public String getUserInfoUrl() {
            return baseApi + userInfo;
        }

        public String getSendMessageUrl() {
            return baseApi + sendMessage;
        }

        public String getUnlinkUrl() {
            return baseApi + unlink;
        }
    }
}
