package sanets.dev.inventoryservice.kafka.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import sanets.dev.inventoryservice.kafka.event.orderservice.OrderCreatedEvent;
import sanets.dev.inventoryservice.kafka.event.InventoryReservedEvent;
import sanets.dev.inventoryservice.kafka.event.InventoryFailedEvent;
import sanets.dev.inventoryservice.service.InventoryService;
import sanets.dev.inventoryservice.kafka.producer.InventoryProducer;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final InventoryService inventoryService;
    private final InventoryProducer inventoryProducer;

    @KafkaListener(topics = "order-created-events-topic", groupId = "inventory-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("Inventory got order id: " + event.orderId());

        try {
            inventoryService.reserveItems(event.orderItemList());

            InventoryReservedEvent successEvent = new InventoryReservedEvent(
                    event.orderId(),
                    event.userId(),
                    event.totalAmount()
            );
            inventoryProducer.sendInventoryReservedEvent(successEvent);

        } catch (RuntimeException e) {
            System.err.println("Reservation failed: " + e.getMessage());

            InventoryFailedEvent failedEvent = new InventoryFailedEvent(
                    event.orderId(),
                    e.getMessage()
            );

            inventoryProducer.sendInventoryFailedEvent(failedEvent);
        }
    }
}