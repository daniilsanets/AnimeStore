package sanets.dev.paymentservice.kafka.event.inventoryservice;


import java.math.BigDecimal;

public record InventoryReservedEvent(
        Long userId,
        Long orderId,
        BigDecimal totalAmount
){}