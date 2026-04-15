package sanets.dev.paymentservice.kafka.event;

import java.math.BigDecimal;

public record PaymentProcessedEvent(
    Long userId,
    Long orderId,
    BigDecimal totalAmount
) {
}
