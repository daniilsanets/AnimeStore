package sanets.dev.inventoryservice.kafka.producer;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import sanets.dev.inventoryservice.kafka.event.InventoryFailedEvent;
import sanets.dev.inventoryservice.kafka.event.InventoryReservedEvent;

@Component
@AllArgsConstructor
public class InventoryProducer {

    KafkaTemplate<String, Object> kafkaTemplate;

    public void sendInventoryReservedEvent(InventoryReservedEvent event){
        kafkaTemplate.send("inventory-reserved-events-topic", String.valueOf(event.orderId()), event);
        System.out.println("Items was reserved successfully");
    }

    public void sendInventoryFailedEvent(InventoryFailedEvent event){
        kafkaTemplate.send("inventory-failed-events-topic", String.valueOf(event.orderId()), event);
        System.out.println("Items cannot be reserved because of: " + event.reason());
    }
}
