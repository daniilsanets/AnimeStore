package sanets.dev.inventoryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sanets.dev.inventoryservice.client.dto.OrderResponseDTO;
import sanets.dev.inventoryservice.config.FeignConfig;

@FeignClient(name = "order-service", configuration = FeignConfig.class)
public interface OrderClient {

    @GetMapping("/api/v1/orders/{orderId}")
    OrderResponseDTO getOrderById(@PathVariable("orderId") Long id);
}
