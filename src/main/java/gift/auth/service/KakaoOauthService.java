package gift.auth.service;

import gift.auth.config.KakaoOauthProperties;
import gift.auth.domain.KakaoAuth;
import gift.auth.domain.MemberAuth;
import gift.auth.domain.TokenInfo;
import gift.auth.dto.KakaoTokenResponseDto;
import gift.auth.dto.KakaoUserInfoResponseDto;
import gift.auth.dto.LoginResponseDto;
import gift.auth.repository.KakaoAuthJpaRepository;
import gift.auth.repository.MemberAuthJpaRepository;
import gift.auth.utils.EmailUtils;
import gift.member.domain.Member;
import gift.member.domain.MemberType;
import gift.member.repository.MemberJpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoOauthService {

    private final MemberAuthJpaRepository memberAuthJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final KakaoAuthJpaRepository kakaoAuthJpaRepository;
    private final KakaoApiClient kakaoApiClient;
    private final TokenService tokenService;
    private final KakaoTokenService kakaoTokenService;
    private final KakaoOauthProperties properties;

    public KakaoOauthService(MemberAuthJpaRepository memberAuthJpaRepository,
        MemberJpaRepository memberJpaRepository, KakaoAuthJpaRepository kakaoAuthJpaRepository,
        KakaoApiClient kakaoApiClient, TokenService tokenService,
        KakaoTokenService kakaoTokenService,
        KakaoOauthProperties properties) {
        this.memberAuthJpaRepository = memberAuthJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.kakaoAuthJpaRepository = kakaoAuthJpaRepository;
        this.kakaoApiClient = kakaoApiClient;
        this.tokenService = tokenService;
        this.kakaoTokenService = kakaoTokenService;
        this.properties = properties;
    }

    @Transactional
    public LoginResponseDto loginKakaoMember(String authorizationCode) {
        KakaoTokenResponseDto tokenResponse = kakaoApiClient.getAccessToken(authorizationCode);
        KakaoUserInfoResponseDto userInfo = kakaoApiClient.getUserInfo(tokenResponse.accessToken());

        //비즈니스 정책에 따른 이메일 생성
        String email = EmailUtils.createEmailByKakaoId(userInfo.id());

        //기존 카카오 소셜 회원 존재 여부 검증
        Optional<MemberAuth> memberAuth = memberAuthJpaRepository.findByEmail(email);
        Long memberId;

        //존재하면 로그인
        if (memberAuth.isPresent()) {
            memberId = memberAuth.get().getId();
            kakaoTokenService.updateKakaoTokenInfo(memberId, tokenResponse.accessToken(),
                tokenResponse.refreshToken());
        }
        //존재안하면 회원가입
        else {
            memberId = registerKakaoMember(userInfo.properties().nickname(), email,
                tokenResponse.accessToken(), tokenResponse.refreshToken(),
                tokenResponse.expiresIn(), tokenResponse.refreshTokenExpiresIn());
        }
        //서비스 자체 JWT발급
        TokenInfo tokenInfo = tokenService.generateAndUpdateBearerTokenInfo(memberId, email);
        return LoginResponseDto.from(tokenInfo);
    }

    private Long registerKakaoMember(String nickName, String email, String kakaoAccessToken,
        String kakaoRefreshToken,
        Integer expiresIn, Integer refreshExpiresIn) {
        Member member = Member.of(nickName, MemberType.KAKAO);
        Long memberId = memberJpaRepository.save(member).getId();

        MemberAuth memberAuth = MemberAuth.withId(memberId, email, "{noop}SOCIAL_LOGIN_USER");
        memberAuthJpaRepository.save(memberAuth);

        KakaoAuth kakaoAuth = KakaoAuth.withId(memberId, kakaoAccessToken, kakaoRefreshToken,
            expiresIn, refreshExpiresIn);
        kakaoAuthJpaRepository.save(kakaoAuth);
        return memberId;
    }

    public String createAuthorizeRedirectUrl() {
        return UriComponentsBuilder.fromUriString(properties.authorizeUri())
            .queryParam("scope", "talk_message")
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", properties.redirectUri())
            .queryParam("client_id", properties.clientId())
            .build()
            .toUriString();
    }
}
