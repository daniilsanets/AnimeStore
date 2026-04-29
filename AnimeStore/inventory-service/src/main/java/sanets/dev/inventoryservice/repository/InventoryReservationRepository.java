package sanets.dev.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sanets.dev.inventoryservice.model.InventoryReservation;

import java.util.Optional;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, Long> {
    Optional<InventoryReservation> findById(Long id);

    Optional<InventoryReservation> getInventoryReservationsByOrderId(Long orderId);
}
