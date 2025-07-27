package gift.auth.service;

import gift.auth.config.KakaoOauthProperties;
import gift.auth.domain.MemberAuth;
import gift.auth.domain.TokenInfo;
import gift.auth.dto.KakaoTokenResponseDto;
import gift.auth.dto.KakaoUserInfoResponseDto;
import gift.auth.dto.LoginResponseDto;
import gift.auth.dto.RegisterMemberResponseDto;
import gift.auth.exception.DuplicatedEmailException;
import gift.auth.repository.MemberAuthJpaRepository;
import gift.auth.utils.EmailUtils;
import gift.member.domain.Member;
import gift.member.domain.MemberType;
import gift.member.repository.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoOauthService {

    private final MemberAuthJpaRepository memberAuthJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final KakaoApiClient kakaoApiClient;
    private final TokenService tokenService;
    private final KakaoOauthProperties properties;

    public KakaoOauthService(MemberAuthJpaRepository memberAuthJpaRepository,
        MemberJpaRepository memberJpaRepository, KakaoApiClient kakaoApiClient,
        TokenService tokenService, KakaoOauthProperties properties) {
        this.memberAuthJpaRepository = memberAuthJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.kakaoApiClient = kakaoApiClient;
        this.tokenService = tokenService;
        this.properties = properties;
    }

    public LoginResponseDto loginKakaoUser(String authorizationCode){
        KakaoTokenResponseDto tokenResponse = kakaoApiClient.getAccessToken(authorizationCode);
        KakaoUserInfoResponseDto userInfo = kakaoApiClient.getUserInfo(tokenResponse.accessToken());

        //비즈니스 정책에 따른 이메일 생성
        String email = EmailUtils.createEmailByKakaoId(userInfo.id());

        //이메일 중복 검증
        if(memberAuthJpaRepository.findByEmail(email).isPresent()){
            throw new DuplicatedEmailException();
        }

        Member member = Member.of(userInfo.properties().nickname(), MemberType.KAKAO);
        Long memberId = memberJpaRepository.save(member).getId();

        MemberAuth memberAuth = MemberAuth.withId(memberId, email, "{noop}SOCIAL_LOGIN_USER");
        memberAuthJpaRepository.save(memberAuth);

        //서비스 자체 JWT발급
        TokenInfo tokenInfo = tokenService.generateBearerTokenInfo(memberId, email);
        return LoginResponseDto.from(tokenInfo);
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
