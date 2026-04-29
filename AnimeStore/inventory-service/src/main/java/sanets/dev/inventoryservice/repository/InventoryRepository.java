package sanets.dev.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanets.dev.inventoryservice.model.Inventory;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);

    @Modifying
    @Query("UPDATE Inventory i SET i.availableQuantity = i.availableQuantity - :qty, i.reservedQuantity = i.reservedQuantity + :qty WHERE i.productId = :productId AND i.availableQuantity >= :qty")
    int attemptReservation(@Param("productId") Long productId, @Param("qty") Integer qty);
}
