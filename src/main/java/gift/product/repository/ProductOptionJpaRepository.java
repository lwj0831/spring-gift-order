package gift.product.repository;

import gift.product.domain.ProductOption;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductOptionJpaRepository extends JpaRepository<ProductOption, Long> {

    @Query("SELECT o.name FROM ProductOption o WHERE o.product.id = :productId AND o.name IN :names")
    List<String> findExistingNamesByProductIdAndNameIn(@Param("productId") Long productId,
        @Param("names") Collection<String> names);


    @Query("select po from ProductOption po where po.product.id = :productId")
    Page<ProductOption> findAllByProductId(@Param("productId") Long productId, Pageable pageable);

}
