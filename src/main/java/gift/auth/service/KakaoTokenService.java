package gift.auth.service;

import gift.auth.domain.KakaoAuth;
import gift.auth.dto.KakaoTokenResponseDto;
import gift.auth.exception.KakaoReauthenticationRequiredException;
import gift.auth.repository.KakaoAuthJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KakaoTokenService {

    private final KakaoAuthJpaRepository kakaoAuthJpaRepository;
    private final KakaoApiClient kakaoApiClient;

    public KakaoTokenService(KakaoAuthJpaRepository kakaoAuthJpaRepository,
        KakaoApiClient kakaoApiClient) {
        this.kakaoAuthJpaRepository = kakaoAuthJpaRepository;
        this.kakaoApiClient = kakaoApiClient;
    }

    @Transactional
    public void updateKakaoTokenInfo(Long memberId, String accessToken, String refreshToken) {
        kakaoAuthJpaRepository.updateKakaoToken(memberId, accessToken, refreshToken);
    }

    @Transactional
    public String getValidAccessTokenOrRefresh(Long memberId) {
        KakaoAuth kakaoAuth = kakaoAuthJpaRepository.findById(memberId)
            .orElseThrow(IllegalArgumentException::new);

        //카카오 엑세스 토큰 만료 여부 검증
        if (kakaoAuth.isAccessTokenExpired()) {
            //카카오 리프레쉬 토큰 만료 여부 검증
            if (kakaoAuth.isRefreshTokenExpired()) {
                throw new KakaoReauthenticationRequiredException();
            }
            KakaoTokenResponseDto tokenResponse = kakaoApiClient.refreshAccessToken(
                kakaoAuth.getRefreshToken());
            kakaoAuth.updateTokenInfo(tokenResponse.accessToken(), tokenResponse.refreshToken());

            return tokenResponse.accessToken();
        }
        return kakaoAuth.getAccessToken();
    }

}
