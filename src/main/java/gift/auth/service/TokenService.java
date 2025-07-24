package gift.auth.service;

import gift.auth.domain.JwtUtils;
import gift.auth.domain.MemberAuth;
import gift.auth.domain.TokenInfo;
import gift.auth.repository.MemberAuthJpaRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TokenService {

    private final JwtUtils jwtUtils;
    private final MemberAuthJpaRepository memberAuthRepository;
    private static final String AUTHORIZATION = "Authorization";
    private static final String TOKEN_TYPE_BEARER = "bearer";
    private static final String BEARER_PREFIX = "Bearer ";

    public TokenService(JwtUtils jwtUtils, MemberAuthJpaRepository memberAuthRepository) {
        this.jwtUtils = jwtUtils;
        this.memberAuthRepository = memberAuthRepository;
    }

    @Transactional
    public TokenInfo generateBearerTokenInfo(Long memberId, String email) {
        String accessToken = jwtUtils.createToken(memberId, email, List.of());
        String refreshToken = jwtUtils.createRefreshToken(memberId);

        MemberAuth memberAuth = memberAuthRepository.findById(memberId)
            .orElseThrow(IllegalArgumentException::new);
        memberAuth.updateRefreshToken(refreshToken);

        long accessTokenExpiresIn = jwtUtils.getAccessTokenExpirationTime();
        long refreshTokenExpiresIn = jwtUtils.getRefreshTokenExpirationTime();

        return new TokenInfo(TOKEN_TYPE_BEARER, accessToken, accessTokenExpiresIn, refreshToken,
            refreshTokenExpiresIn);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean isValidToken(String token) {
        return jwtUtils.validateToken(token);
    }

    public String getEmail(String token) {
        return jwtUtils.getEmail(token);
    }

    public Long getUserId(String token) {
        return jwtUtils.getUserId(token);
    }
}
