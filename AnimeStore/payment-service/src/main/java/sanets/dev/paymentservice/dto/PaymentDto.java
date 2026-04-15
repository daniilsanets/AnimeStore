package sanets.dev.paymentservice.dto;

import java.math.BigDecimal;

public record PaymentDto(
    Long userId,
    Long orderId,
    BigDecimal totalAmount
) {
}
