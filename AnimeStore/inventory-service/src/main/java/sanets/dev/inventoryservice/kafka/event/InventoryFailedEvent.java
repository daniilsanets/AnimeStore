package sanets.dev.inventoryservice.kafka.event;

public record InventoryFailedEvent(
        Long orderId,
        String reason
) {}