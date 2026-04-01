package sanets.dev.orderservice.client.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CatalogProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Boolean active;
}
