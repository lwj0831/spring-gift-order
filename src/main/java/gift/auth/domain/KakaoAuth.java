package gift.auth.domain;

import gift.global.common.jpa.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "kakao_auth")
public class KakaoAuth extends TimeBaseEntity {

    @Id
    private Long id;
    @Column(nullable = false)
    private String accessToken;
    @Column(nullable = false)
    private String refreshToken;
    @Column(nullable = false)
    private LocalDateTime accessTokenExpiresAt;
    @Column(nullable = false)
    private LocalDateTime refreshTokenExpiresAt;

    protected KakaoAuth() {
    }

    private KakaoAuth(Long id, String accessToken, String refreshToken, Integer accessExpiresIn,
        Integer refreshExpiresIn) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        LocalDateTime now = LocalDateTime.now();
        this.accessTokenExpiresAt = now.plusSeconds(accessExpiresIn);
        this.refreshTokenExpiresAt = now.plusSeconds(refreshExpiresIn);
    }

    public static KakaoAuth withId(Long id, String accessToken, String refreshToken,
        Integer accessExpiresIn, Integer refreshExpiresIn) {
        return new KakaoAuth(id, accessToken, refreshToken, accessExpiresIn, refreshExpiresIn);
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void updateTokenInfo(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public boolean isAccessTokenExpired() {
        return LocalDateTime.now().isAfter(accessTokenExpiresAt);
    }

    public boolean isRefreshTokenExpired() {
        return LocalDateTime.now().isAfter(refreshTokenExpiresAt);
    }
}
