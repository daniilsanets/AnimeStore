package sanets.dev.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sanets.dev.orderservice.config.FeignConfig;
import sanets.dev.orderservice.client.dto.CatalogProductDto;

@FeignClient(name = "catalog-service", configuration = FeignConfig.class)
public interface CatalogClient {

    @GetMapping("/api/v1/catalog/products/{id}")
    CatalogProductDto getProductById(@PathVariable("id") Long id);
}
