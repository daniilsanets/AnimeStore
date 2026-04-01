package sanets.dev.orderservice.entity;

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
