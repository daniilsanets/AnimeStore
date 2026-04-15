package sanets.dev.orderservice.kafka.producer;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sanets.dev.orderservice.entity.Order;
import sanets.dev.orderservice.kafka.event.OrderCreatedEvent;

@Service
@AllArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreateEvent(OrderCreatedEvent event) {
        kafkaTemplate.send("order-created-events-topic", String.valueOf(event.orderId()), event);
        System.out.println("Order created: " + event.orderId());
    }
}
