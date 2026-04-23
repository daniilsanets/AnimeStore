package sanets.dev.orderservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sanets.dev.orderservice.dto.OrderStatusUpdateRequestDTO;
import sanets.dev.orderservice.kafka.event.paymentservice.PaymentCanceledEvent;
import sanets.dev.orderservice.model.Status;
import sanets.dev.orderservice.service.OrderService;

@Service
@RequiredArgsConstructor
public class PaymentFailedListener {

    private final OrderService orderService;

    @KafkaListener(topics = "payment-canceled-events-topic", groupId = "order-service-group")
    public void handlePaymentCanceledEvent(PaymentCanceledEvent event) {

        System.out.println("Order will be moved into CANCELED status because of payment fail with id: " + event.orderId());
        var orderStatusUpdateRequestDTO = new OrderStatusUpdateRequestDTO();

        orderStatusUpdateRequestDTO.setNewStatus(Status.CANCELLED);
        orderStatusUpdateRequestDTO.setReason(event.reason());

        orderService.updateOrderStatus(event.orderId(), orderStatusUpdateRequestDTO);
        System.out.println("Order was moved into CANCELED status because of payment fail");
    }
}
