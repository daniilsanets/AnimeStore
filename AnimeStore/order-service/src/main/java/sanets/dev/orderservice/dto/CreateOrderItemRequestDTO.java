package sanets.dev.orderservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderItemRequestDTO {
    @NotBlank
    private Long productId;

    @NotBlank
    @Max(100)
    private Integer quantity;
}
