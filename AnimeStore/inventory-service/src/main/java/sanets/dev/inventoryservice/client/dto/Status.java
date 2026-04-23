package sanets.dev.inventoryservice.client.dto;

public enum Status {
    NEW,
    RESERVE_PENDING,
    RESERVED,
    PAYMENT_PENDING,
    PAID,
    COMPLETED,
    CANCELLED,
    FAILED
}
