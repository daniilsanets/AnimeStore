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

    public void sendOrderCreateEvent(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(order.getId(), order.getUserId());
        kafkaTemplate.send("orders-events-topic", String.valueOf(order.getId()), event);
        System.out.println("Order created: " + order.getId());
    }
}
