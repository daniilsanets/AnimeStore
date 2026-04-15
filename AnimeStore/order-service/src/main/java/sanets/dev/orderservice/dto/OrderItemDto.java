package sanets.dev.orderservice.dto;

public record OrderItemDto(
        Long productId,
        Integer quantity
) {}
