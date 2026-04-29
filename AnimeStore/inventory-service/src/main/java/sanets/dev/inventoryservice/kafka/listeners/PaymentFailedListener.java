package sanets.dev.inventoryservice.kafka.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sanets.dev.inventoryservice.kafka.event.InventoryFailedEvent;
import sanets.dev.inventoryservice.kafka.event.paymentservice.PaymentCanceledEvent;
import sanets.dev.inventoryservice.kafka.producer.InventoryProducer;
import sanets.dev.inventoryservice.service.InventoryService;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentFailedListener {
    private final InventoryService inventoryService;
    private final InventoryProducer inventoryProducer;

    @KafkaListener(topics = "payment-canceled-events-topic", groupId = "inventory-group")
    public void handlePaymentFailed(PaymentCanceledEvent event){
        log.info("Inventory got PaymentCanceledEvent sent successfully");

        try {
            inventoryService.cancelReservation(event.orderId());
            log.info("PaymentCanceledEvent canceled all items successfully");
        } catch (RuntimeException ex) {
            inventoryProducer.sendInventoryFailedEvent(new InventoryFailedEvent(
                    event.orderId(),
                    ex.getMessage()
            ));
            log.debug(ex.getMessage(), ex);
        }
    }
}
