package sanets.dev.inventoryservice.kafka.producer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import sanets.dev.inventoryservice.kafka.event.InventoryFailedEvent;
import sanets.dev.inventoryservice.kafka.event.InventoryReservedEvent;

@Component
@AllArgsConstructor
@Slf4j
public class InventoryProducer {

    KafkaTemplate<String, Object> kafkaTemplate;

    public void sendInventoryReservedEvent(InventoryReservedEvent event){
        kafkaTemplate.send("inventory-reserved-events-topic", String.valueOf(event.orderId()), event);
        log.info("Items was reserved successfully");
    }

    public void sendInventoryFailedEvent(InventoryFailedEvent event){
        kafkaTemplate.send("inventory-failed-events-topic", String.valueOf(event.orderId()), event);
        log.info("Items cannot be reserved because of: {}", event.reason());
    }
}
