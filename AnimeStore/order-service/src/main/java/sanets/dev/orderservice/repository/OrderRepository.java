package sanets.dev.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sanets.dev.orderservice.entity.Order;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findById(Long id);
    Optional<Order> findByIdempotencyKey(java.util.UUID idempotencyKey);
}
