package gift.wishlist.repository;

import gift.wishlist.domain.WishItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishItemJpaRepository extends JpaRepository<WishItem, Long> {

    @Query("select we from WishItem we where we.member.id = :memberId and we.product.id = :productId")
    Optional<WishItem> findByMemberIdAndProductId(@Param("memberId") Long memberId,
        @Param("productId") Long productId);

    @EntityGraph(attributePaths = {"product"})
    @Query("select we from WishItem we where we.member.id = :memberId")
    Page<WishItem> findAllWithProductByMemberId(@Param("memberId") Long memberId,
        Pageable pageable);

}
