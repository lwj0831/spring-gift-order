package gift.infra.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.config.KakaoOauthProperties;
import gift.infra.kakao.dto.KakaoMessageResponseDto;
import gift.infra.kakao.dto.KakaoTokenResponseDto;
import gift.infra.kakao.dto.KakaoUserInfoResponseDto;
import gift.infra.kakao.dto.TextTemplate;
import gift.infra.kakao.exception.KakaoApiClientException;
import gift.infra.kakao.exception.KakaoApiServerException;
import java.net.URI;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoApiClient {

    private final RestClient restClient;
    private final KakaoOauthProperties properties;
    private final ObjectMapper objectMapper;

    public KakaoApiClient(RestClient.Builder builder, KakaoOauthProperties properties,
        ObjectMapper objectMapper) {
        this.restClient = builder
            .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new KakaoApiClientException(response.getStatusText());
            })
            .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                throw new KakaoApiServerException(response.getStatusText());
            })
            .build();
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public KakaoTokenResponseDto getAccessToken(String code) {
        URI uri = UriComponentsBuilder
            .fromUriString(properties.urls().getTokenUrl())
            .build()
            .toUri();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUri());
        body.add("code", code);
        body.add("client_secret", properties.clientSecret());

        return restClient.post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(KakaoTokenResponseDto.class);
    }

    public KakaoTokenResponseDto refreshAccessToken(String refreshToken) {
        URI uri = UriComponentsBuilder
            .fromUriString(properties.urls().getTokenUrl())
            .build()
            .toUri();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("refresh_token", refreshToken);
        body.add("client_secret", properties.clientSecret());

        return restClient.post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(KakaoTokenResponseDto.class);
    }

    public KakaoUserInfoResponseDto getUserInfo(String kakaoAccessToken) {
        URI uri = UriComponentsBuilder
            .fromUriString(properties.urls().getUserInfoUrl())
            .build()
            .toUri();

        return restClient.get()
            .uri(uri)
            .header("Authorization", "Bearer " + kakaoAccessToken)
            .retrieve()
            .body(KakaoUserInfoResponseDto.class);
    }

    public KakaoMessageResponseDto sendKakaoMessage(String kakaoAccessToken, String message) {
        URI uri = UriComponentsBuilder
            .fromUriString(properties.urls().getSendMessageUrl())
            .build()
            .toUri();

        TextTemplate template = new TextTemplate(message, "https://spring-gift.com");
        String templateJson;
        try {
            templateJson = objectMapper.writeValueAsString(template);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("메시지 템플릿 직렬화 실패", e);
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateJson);

        return restClient.post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + kakaoAccessToken)
            .body(body)
            .retrieve()
            .body(KakaoMessageResponseDto.class);
    }

}