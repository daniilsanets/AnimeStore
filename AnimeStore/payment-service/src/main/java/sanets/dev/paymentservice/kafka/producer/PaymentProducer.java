package sanets.dev.paymentservice.kafka.producer;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import sanets.dev.paymentservice.kafka.event.PaymentCanceledEvent;
import sanets.dev.paymentservice.kafka.event.PaymentProcessedEvent;

@Component
@AllArgsConstructor
public class PaymentProducer {
    KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentSuccessfullyEvent(PaymentProcessedEvent event){
        kafkaTemplate.send("payment-processed-events-topic", event);
        System.out.println(String.join("PaymentSuccessfullyEvent sent successfully", event.orderId().toString()));
    }

    public void sendPaymentCanceledEvent(PaymentCanceledEvent event){
        kafkaTemplate.send("payment-canceled-events-topic", event);
        System.out.println(String.join("PaymentCanceledEvent sent successfully", event.orderId().toString()));
    }
}
