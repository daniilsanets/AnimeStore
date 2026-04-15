package sanets.dev.paymentservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sanets.dev.paymentservice.kafka.event.inventoryservice.InventoryReservedEvent;
import sanets.dev.paymentservice.kafka.producer.PaymentProducer;
import sanets.dev.paymentservice.service.PaymentService;

@Service
@RequiredArgsConstructor
public class InventoryReservedListener {

    private final PaymentProducer producer;
    private final PaymentService paymentService;

    @KafkaListener(topics = "inventory-reserved-events-topic", groupId = "payment-group")
    public void handleOrderCreated(InventoryReservedEvent event) {
        System.out.println("Dear user) " + event.userId() +
                ", ur order id: " + event.orderId() + " successfully processed");

        producer.sendPaymentSuccessfullyEvent(paymentService.doPayment(event));
    }



}
