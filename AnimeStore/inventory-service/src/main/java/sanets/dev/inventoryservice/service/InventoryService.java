package sanets.dev.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sanets.dev.inventoryservice.kafka.event.orderservice.OrderItemDto;
import sanets.dev.inventoryservice.model.InventoryReservation;
import sanets.dev.inventoryservice.repository.InventoryRepository;
import sanets.dev.inventoryservice.model.Inventory;
import sanets.dev.inventoryservice.repository.InventoryReservationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryReservationRepository inventoryReservationRepository;

    @Transactional
    public void reserveItems(Long orderId, List<OrderItemDto> items) {

        List<InventoryReservation> reservations = new ArrayList<>();

        for (OrderItemDto item : items) {
            int updatedRows = inventoryRepository.attemptReservation(item.productId(), item.quantity());
            if (updatedRows == 0) {
                throw new IllegalArgumentException("Could not reserve item " + item.productId() +
                        " (not found or not enough quantity)");
            }

            reservations.add(toInventoryReservation(orderId, item));
        }

        inventoryReservationRepository.saveAll(reservations);
    }

    @Transactional
    public void cancelReservation(Long orderId) {
        inventoryReservationRepository.getInventoryReservationsByOrderId(orderId)
                .ifPresent(inventoryReservation ->
                        {
                            Inventory inventory = inventoryRepository.findByProductId(inventoryReservation.getProductId())
                            .orElseThrow(() -> new RuntimeException("Item with ID " + inventoryReservation.getProductId() + " not found in storage"));

                            inventory.setAvailableQuantity(inventory.getAvailableQuantity() + inventoryReservation.getQuantity());
                            inventory.setReservedQuantity(inventory.getReservedQuantity() - inventoryReservation.getQuantity());

                            inventoryRepository.save(inventory);
                            inventoryReservationRepository.delete(inventoryReservation);
                        });
    }

    //mapper responsibility
    private InventoryReservation toInventoryReservation(Long orderId, OrderItemDto item) {
        return InventoryReservation.builder()
                .orderId(orderId)
                .productId(item.productId())
                .quantity(item.quantity())
                .build();
    }

}