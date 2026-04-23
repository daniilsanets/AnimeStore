package sanets.dev.inventoryservice.kafka.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sanets.dev.inventoryservice.client.OrderClient;
import sanets.dev.inventoryservice.client.dto.OrderResponseDTO;
import sanets.dev.inventoryservice.kafka.event.InventoryFailedEvent;
import sanets.dev.inventoryservice.kafka.event.paymentservice.PaymentCanceledEvent;
import sanets.dev.inventoryservice.kafka.producer.InventoryProducer;
import sanets.dev.inventoryservice.service.InventoryService;

@Service
@RequiredArgsConstructor
public class PaymentFailedListener {
    private final InventoryService inventoryService;
    private final InventoryProducer inventoryProducer;
    private final OrderClient orderClient;

    @KafkaListener(topics = "payment-canceled-events-topic", groupId = "inventory-group")
    public void handlePaymentFailed(PaymentCanceledEvent event){
        System.out.println("Inventory got PaymentCanceledEvent sent successfully" );

        OrderResponseDTO order = orderClient.getOrderById(event.orderId());

        try {
            inventoryService.cancelReservedItems(order.getItems());
            System.out.println("PaymentCanceledEvent canceled all items successfully");
        } catch (RuntimeException ex) {
            inventoryProducer.sendInventoryFailedEvent(new InventoryFailedEvent(
                    event.orderId(),
                    ex.getMessage()
            ));
        }

        //How can I get order info? Maybe use openfeign because we will have a little bit failed payments I think and will it be enough
        //from this point of view but I need to create high-load system architecture and how to do that??
    }
}
