package sanets.dev.orderservice.kafka.event;

public record OrderCreatedEvent(
        Long orderId,
        Long userId
) {}
