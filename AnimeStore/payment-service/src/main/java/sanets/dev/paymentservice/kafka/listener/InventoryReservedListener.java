package sanets.dev.paymentservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sanets.dev.paymentservice.kafka.event.inventoryservice.InventoryReservedEvent;
import sanets.dev.paymentservice.kafka.producer.PaymentProducer;
import sanets.dev.paymentservice.service.PaymentService;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryReservedListener {

    private final PaymentProducer producer;
    private final PaymentService paymentService;

    @KafkaListener(topics = "inventory-reserved-events-topic", groupId = "payment-group")
    public void handleOrderCreated(InventoryReservedEvent event) {
        log.info("Dear user) {}, ur order id: {} successfully processed", event.userId(), event.orderId());
        producer.sendPaymentSuccessfullyEvent(paymentService.doPayment(event));
    }

}
