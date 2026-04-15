package sanets.dev.orderservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sanets.dev.orderservice.kafka.event.paymentservice.PaymentProcessedEvent;
import sanets.dev.orderservice.service.OrderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentProcessedListener {
     private final OrderService orderService;

     @KafkaListener(topics = "payment-processed-events-topic", groupId = "order-group")
     public void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        orderService.paymentProcessed(event);
        log.info("Received Payment Processed Event : {}", event.orderId());
     }
}
