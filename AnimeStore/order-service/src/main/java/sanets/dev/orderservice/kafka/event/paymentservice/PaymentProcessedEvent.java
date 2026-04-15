package sanets.dev.orderservice.kafka.event.paymentservice;

import java.math.BigDecimal;

public record PaymentProcessedEvent(
    Long userId,
    Long orderId,
    BigDecimal totalAmount
) {
}
