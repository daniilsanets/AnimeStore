package sanets.dev.inventoryservice.kafka.event.orderservice;

public record OrderItemDto(
        Long productId,
        Integer quantity
) {}
