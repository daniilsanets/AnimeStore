package sanets.dev.inventoryservice.kafka.event.paymentservice;

public record PaymentCanceledEvent(
        Long userId,
        Long orderId,
        String reason
) {
}
