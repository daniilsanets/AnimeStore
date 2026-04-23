package sanets.dev.orderservice.kafka.event.paymentservice;

public record PaymentCanceledEvent(
        Long userId,
        Long orderId,
        String reason
) {
}
