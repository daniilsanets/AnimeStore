package sanets.dev.notificationservice.kafka.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sanets.dev.notificationservice.kafka.event.OrderCreatedEvent;

@Service
public class NotificationListener {

    @KafkaListener(topics = "orders-events-topic", groupId = "notification-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("Dear user " + event.userId() +
                ", ur order id: " + event.orderId() + " successfully created");
    }
}
