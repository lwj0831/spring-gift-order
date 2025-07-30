package gift.auth.service;

import gift.auth.domain.MemberAuth;
import gift.auth.domain.TokenInfo;
import gift.auth.dto.LoginRequestDto;
import gift.auth.dto.LoginResponseDto;
import gift.auth.dto.RefreshTokenRequestDto;
import gift.auth.dto.RegisterMemberRequestDto;
import gift.auth.dto.RegisterMemberResponseDto;
import gift.auth.exception.DuplicatedEmailException;
import gift.auth.exception.ExpiredTokenException;
import gift.auth.exception.InvalidLoginException;
import gift.auth.exception.InvalidTokenException;
import gift.auth.exception.PasswordMismatchException;
import gift.auth.repository.MemberAuthJpaRepository;
import gift.member.domain.Member;
import gift.member.domain.MemberType;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberAuthJpaRepository memberAuthRepository;
    private final MemberJpaRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(MemberAuthJpaRepository memberAuthRepository,
        MemberJpaRepository memberRepository,
        PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.memberAuthRepository = memberAuthRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public RegisterMemberResponseDto registerMember(RegisterMemberRequestDto dto) {
        String email = dto.email();
        if (memberAuthRepository.findByEmail(email).isPresent()) {
            throw new DuplicatedEmailException();
        }

        Member member = Member.of(dto.username(), MemberType.GENERAL);
        Long memberId = memberRepository.save(member).getId();
        String encodedPassword = passwordEncoder.encode(dto.password());

        MemberAuth memberAuth = MemberAuth.withId(memberId, dto.email(), encodedPassword);
        memberAuthRepository.save(memberAuth);

        TokenInfo tokenInfo = tokenService.generateAndUpdateBearerTokenInfo(memberId, email);
        return RegisterMemberResponseDto.from(tokenInfo, memberId);
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        String email = dto.email();
        MemberAuth memberAuth = memberAuthRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException(email));

        Member member = memberRepository.findById(memberAuth.getId())
            .orElseThrow(() -> new MemberNotFoundException(memberAuth.getId()));

        //카카오 소셜 회원은 일반 로그인 실패
        if (member.isKakaoUser()) {
            throw new InvalidLoginException();
        }

        //비밀번호 검증
        if (!passwordEncoder.matches(dto.password(), memberAuth.getPassword())) {
            throw new PasswordMismatchException();
        }

        TokenInfo tokenInfo = tokenService.generateAndUpdateBearerTokenInfo(member.getId(), email);
        return LoginResponseDto.from(tokenInfo);
    }

    @Transactional
    public LoginResponseDto refreshToken(RefreshTokenRequestDto dto) {
        String refreshToken = dto.refreshToken();

        //리프레쉬 토큰 만료 여부 검증
        if (!tokenService.isValidToken(refreshToken)) {
            throw new ExpiredTokenException();
        }

        String email = tokenService.getEmail(refreshToken);
        Long memberId = tokenService.getUserId(refreshToken);
        MemberAuth memberAuth = findMemberAuthOrThrow(memberId);

        //리프레쉬 토큰 일치 여부 검증
        if (!memberAuth.matchRefreshToken(refreshToken)) {
            throw new InvalidTokenException();
        }

        TokenInfo tokenInfo = tokenService.generateAndUpdateBearerTokenInfo(memberId, email);
        return LoginResponseDto.from(tokenInfo);
    }

    @Transactional
    public void logout(String email) {
        MemberAuth memberAuth = memberAuthRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException(email));

        memberAuth.expiredRefreshToken();
    }

    private MemberAuth findMemberAuthOrThrow(Long id) {
        return memberAuthRepository.findById(id)
            .orElseThrow(() -> new MemberNotFoundException(id));
    }

}
