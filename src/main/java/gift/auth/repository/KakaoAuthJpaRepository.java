package gift.auth.repository;

import gift.auth.domain.KakaoAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface KakaoAuthJpaRepository extends JpaRepository<KakaoAuth, Long> {

    @Modifying
    @Query("update KakaoAuth ka set ka.accessToken = :accessToken, ka.refreshToken = :refreshToken where ka.id = :memberId ")
    void updateKakaoToken(Long memberId, String accessToken, String refreshToken);
}
