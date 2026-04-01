package sanets.dev.orderservice.dto;

import lombok.Builder;
import lombok.Data;
import sanets.dev.orderservice.entity.Status;

import java.math.BigDecimal;

@Data
@Builder
public class CreateOrderResponseDTO {
    private Long orderId;
    private Status status;
    private BigDecimal totalAmount; //total order sum
}
