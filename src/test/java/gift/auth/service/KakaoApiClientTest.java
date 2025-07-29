package gift.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.config.KakaoOauthProperties;
import gift.auth.dto.KakaoTokenResponseDto;
import gift.auth.dto.KakaoUserInfoResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

class KakaoApiClientTest {

    private KakaoApiClient kakaoApiClient;
    private MockRestServiceServer mockServer;
    private KakaoOauthProperties kakaoOauthProperties;

    @BeforeEach
    void setUp() {
        KakaoOauthProperties.Urls urls = mock(KakaoOauthProperties.Urls.class);
        when(urls.getTokenUrl()).thenReturn("https://kauth.kakao.com/oauth/token");
        when(urls.getUserInfoUrl()).thenReturn("https://kapi.kakao.com/v2/user/me");
        when(urls.getSendMessageUrl()).thenReturn(
            "https://kapi.kakao.com/v2/api/talk/memo/default/send");

        kakaoOauthProperties = mock(KakaoOauthProperties.class);
        when(kakaoOauthProperties.urls()).thenReturn(urls);
        when(kakaoOauthProperties.clientId()).thenReturn("client-id");
        when(kakaoOauthProperties.redirectUri()).thenReturn("http://localhost/callback");
        when(kakaoOauthProperties.clientSecret()).thenReturn("client-secret");

        RestClient.Builder restClientBuilder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        kakaoApiClient = new KakaoApiClient(restClientBuilder, kakaoOauthProperties,
            new ObjectMapper());
    }

    @AfterEach
    void tearDown() {
        mockServer.reset();
    }

    @Test
    void getAccessToken() {
        String responseJson = """
            {
                "token_type": "bearer",
                "access_token": "access-token",
                "expires_in": 3600,
                "refresh_token": "refresh-token",
                "refresh_token_expires_in": 2592000,
                "scope": "profile"
            }
            """;

        MultiValueMap<String, String> expectedForm = new LinkedMultiValueMap<>();
        expectedForm.add("grant_type", "authorization_code");
        expectedForm.add("client_id", "client-id");
        expectedForm.add("redirect_uri", "http://localhost/callback");
        expectedForm.add("code", "auth-code");
        expectedForm.add("client_secret", "client-secret");

        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
            .andExpect(method(org.springframework.http.HttpMethod.POST))
            .andExpect(content().formData(expectedForm))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        KakaoTokenResponseDto result = kakaoApiClient.getAccessToken("auth-code");

        assertAll(
            () -> assertThat(result.tokenType()).isEqualTo("bearer"),
            () -> assertThat(result.accessToken()).isEqualTo("access-token"),
            () -> assertThat(result.expiresIn()).isEqualTo(3600),
            () -> assertThat(result.refreshToken()).isEqualTo("refresh-token"),
            () -> assertThat(result.refreshTokenExpiresIn()).isEqualTo(2592000),
            () -> assertThat(result.scope()).isEqualTo("profile")
        );

        mockServer.verify();
    }

    @Test
    void refreshAccessToken() {
        String responseJson = """
            {
                "token_type": "bearer",
                "access_token": "new-access-token",
                "expires_in": 3600,
                "refresh_token": "new-refresh-token",
                "refresh_token_expires_in": 2592000,
                "scope": "profile"
            }
            """;

        MultiValueMap<String, String> expectedForm = new LinkedMultiValueMap<>();
        expectedForm.add("grant_type", "authorization_code");
        expectedForm.add("client_id", "client-id");
        expectedForm.add("refresh_token", "refresh-token");
        expectedForm.add("client_secret", "client-secret");

        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
            .andExpect(method(org.springframework.http.HttpMethod.POST))
            .andExpect(content().formData(expectedForm))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        KakaoTokenResponseDto result = kakaoApiClient.refreshAccessToken("refresh-token");

        assertAll(
            () -> assertThat(result.tokenType()).isEqualTo("bearer"),
            () -> assertThat(result.accessToken()).isEqualTo("new-access-token"),
            () -> assertThat(result.expiresIn()).isEqualTo(3600),
            () -> assertThat(result.refreshToken()).isEqualTo("new-refresh-token"),
            () -> assertThat(result.refreshTokenExpiresIn()).isEqualTo(2592000),
            () -> assertThat(result.scope()).isEqualTo("profile")
        );

        mockServer.verify();
    }

    @Test
    void getUserInfo() {
        String responseJson = """
            {
                "id": 12345,
                "properties": {
                    "nickname": "테스트유저"
                }
            }
            """;

        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
            .andExpect(method(org.springframework.http.HttpMethod.GET))
            .andExpect(header("Authorization", "Bearer access-token"))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        KakaoUserInfoResponseDto result = kakaoApiClient.getUserInfo("access-token");

        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.id()).isEqualTo(12345L),
            () -> assertThat(result.properties()).isNotNull(),
            () -> assertThat(result.properties().nickname()).isEqualTo("테스트유저")
        );

        mockServer.verify();
    }

    @Test
    void sendKakaoMessage() {
        mockServer.expect(requestTo("https://kapi.kakao.com/v2/api/talk/memo/default/send"))
            .andExpect(method(org.springframework.http.HttpMethod.POST))
            .andExpect(header("Authorization", "Bearer access-token"))
            .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(content().string(containsString("template_object=")))
            .andRespond(withSuccess("0", MediaType.APPLICATION_JSON));

        assertThatNoException().isThrownBy(() ->
            kakaoApiClient.sendKakaoMessage("access-token", "test message")
        );

        mockServer.verify();
    }

    @Test
    void getAccessToken_withError() {
        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
            .andExpect(method(org.springframework.http.HttpMethod.POST))
            .andRespond(withBadRequest().body("Invalid request"));

        assertThatThrownBy(() -> kakaoApiClient.getAccessToken("invalid-code"))
            .isInstanceOf(Exception.class);

        mockServer.verify();
    }

    @Test
    void getUserInfo_withError() {
        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
            .andExpect(method(org.springframework.http.HttpMethod.GET))
            .andExpect(header("Authorization", "Bearer invalid-token"))
            .andRespond(withUnauthorizedRequest().body("Invalid token"));

        assertThatThrownBy(() -> kakaoApiClient.getUserInfo("invalid-token"))
            .isInstanceOf(Exception.class);

        mockServer.verify();
    }
}
