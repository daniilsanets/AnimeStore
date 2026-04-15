package sanets.dev.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sanets.dev.inventoryservice.model.Inventory;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findById(Long id);

    Optional<Inventory> findByProductId(Long productId);
}
