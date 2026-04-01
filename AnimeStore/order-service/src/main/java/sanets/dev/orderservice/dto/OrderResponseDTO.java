package sanets.dev.orderservice.dto;

import lombok.Builder;
import lombok.Data;
import sanets.dev.orderservice.entity.Status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private Status status;
    private BigDecimal totalAmount;
    private String currency;
    private OffsetDateTime createdAt;
    private Set<OrderItemResponseDTO> items;
}
