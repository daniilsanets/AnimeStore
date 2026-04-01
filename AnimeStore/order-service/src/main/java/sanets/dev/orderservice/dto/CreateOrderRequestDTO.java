package sanets.dev.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class CreateOrderRequestDTO {
    @NotBlank
    private Long userId;

    @NotBlank
    private Set<CreateOrderItemRequestDTO> orderItems;
}
