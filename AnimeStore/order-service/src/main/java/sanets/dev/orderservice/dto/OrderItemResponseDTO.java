package sanets.dev.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponseDTO {
    private Long productId;
    private String productNameSnapshot;
    private BigDecimal productPriceSnapshot;
    private Integer quantity;
    private BigDecimal lineTotal;
}
