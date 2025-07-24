package gift.auth.repository;

import gift.auth.domain.MemberAuth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAuthJpaRepository extends JpaRepository<MemberAuth, Long> {

    Optional<MemberAuth> findByEmail(String email);

}
