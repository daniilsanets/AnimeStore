package sanets.dev.catalogservice.dto;

import lombok.Data;
import sanets.dev.catalogservice.entity.ProductType;

import java.math.BigDecimal;

@Data
public class ProductFilter {
    private String name;
    private BigDecimal price;
    private ProductType type;
    private Long categoryId;
    private Boolean active;
}
