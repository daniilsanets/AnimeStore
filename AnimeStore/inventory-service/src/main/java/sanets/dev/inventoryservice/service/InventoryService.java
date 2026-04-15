package sanets.dev.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sanets.dev.inventoryservice.kafka.event.orderservice.OrderItemDto;
import sanets.dev.inventoryservice.kafka.producer.InventoryProducer;
import sanets.dev.inventoryservice.repository.InventoryRepository;
import sanets.dev.inventoryservice.model.Inventory; // Твоя сущность склада

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional
    public void reserveItems(List<OrderItemDto> items) {

        for (OrderItemDto item : items) {

            Inventory inventory = inventoryRepository.findByProductId(item.productId())
                    .orElseThrow(() -> new RuntimeException("Item with ID " + item.productId() + " did not found in storage"));

            if (inventory.getAvailableQuantity() < item.quantity()) {
                throw new RuntimeException("Does not have enough quantity " + item.productId());
            }

            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - item.quantity());
            inventory.setReservedQuantity(inventory.getReservedQuantity() + item.quantity());

            inventoryRepository.save(inventory);
        }
    }
}