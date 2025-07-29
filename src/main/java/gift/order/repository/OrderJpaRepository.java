package gift.order.repository;

import gift.order.domain.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderProducts","orderProducts.product","orderProducts.productOption"})
    Optional<Order> findOrderInfoById(Long id);

}
