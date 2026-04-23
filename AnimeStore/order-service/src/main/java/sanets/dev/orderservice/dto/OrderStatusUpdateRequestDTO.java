package sanets.dev.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import sanets.dev.orderservice.model.Status;

@Data
public class OrderStatusUpdateRequestDTO {
    @NotBlank
    private Status newStatus;

    @NotBlank
    @Length(max = 256)
    private String reason;
}
