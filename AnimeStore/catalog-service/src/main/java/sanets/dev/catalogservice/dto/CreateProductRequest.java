package sanets.dev.catalogservice.dto;

import lombok.Data;
import sanets.dev.catalogservice.entity.ProductType;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private ProductType type;
    private Long categoryId;
    private Boolean active;
}

