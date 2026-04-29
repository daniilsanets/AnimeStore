package sanets.dev.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sanets.dev.paymentservice.kafka.event.PaymentProcessedEvent;
import sanets.dev.paymentservice.kafka.event.inventoryservice.InventoryReservedEvent;
import sanets.dev.paymentservice.model.Payment;
import sanets.dev.paymentservice.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentProcessedEvent doPayment(InventoryReservedEvent event){
        log.info("Payment was done for user " + event.userId() + "order id: " + event.orderId());

        paymentRepository.save(Payment.builder()
                .orderId(event.orderId())
                .userId(event.userId())
                .totalAmount(event.totalAmount())
                .build());

        return new PaymentProcessedEvent(event.userId(), event.orderId(), event.totalAmount());
    }
}
