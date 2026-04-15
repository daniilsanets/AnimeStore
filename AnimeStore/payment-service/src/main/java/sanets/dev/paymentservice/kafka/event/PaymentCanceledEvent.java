package sanets.dev.paymentservice.kafka.event;

public record PaymentCanceledEvent(
        Long userId,
        Long orderId,
        String reason
) {
}
