package sanets.dev.inventoryservice.kafka.event;


import java.math.BigDecimal;

public record InventoryReservedEvent(
        Long userId,
        Long orderId,
        BigDecimal totalAmount
){}
