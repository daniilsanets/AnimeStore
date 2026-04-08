package sanets.dev.notificationservice.kafka.event;

public record OrderCreatedEvent(
        Long orderId,
        Long userId
) {}
