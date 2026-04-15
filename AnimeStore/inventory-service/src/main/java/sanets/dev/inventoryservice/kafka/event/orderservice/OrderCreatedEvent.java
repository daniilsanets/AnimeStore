package sanets.dev.inventoryservice.kafka.event.orderservice;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedEvent(
        Long orderId,
        Long userId,
        BigDecimal totalAmount,
        List<OrderItemDto> orderItemList
) {}
