package sanets.dev.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ApiErrorResponseDTO {
    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
