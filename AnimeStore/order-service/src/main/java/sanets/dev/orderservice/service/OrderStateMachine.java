package sanets.dev.orderservice.service;

import org.springframework.stereotype.Component;
import sanets.dev.orderservice.model.Status;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Component
public class OrderStateMachine {
    private final Map<Status, Set<Status>> allowedTransitions = new EnumMap<>(Status.class);

    public OrderStateMachine() {
        allowedTransitions.put(Status.NEW, EnumSet.of(Status.RESERVE_PENDING, Status.CANCELLED, Status.FAILED));
        allowedTransitions.put(Status.RESERVE_PENDING, EnumSet.of(Status.RESERVED, Status.FAILED));
        allowedTransitions.put(Status.RESERVED, EnumSet.of(Status.PAYMENT_PENDING, Status.CANCELLED, Status.FAILED));
        allowedTransitions.put(Status.PAYMENT_PENDING, EnumSet.of(Status.PAID, Status.FAILED));
        allowedTransitions.put(Status.PAID, EnumSet.of(Status.COMPLETED));
        allowedTransitions.put(Status.COMPLETED, EnumSet.noneOf(Status.class));
        allowedTransitions.put(Status.CANCELLED, EnumSet.noneOf(Status.class));
        allowedTransitions.put(Status.FAILED, EnumSet.noneOf(Status.class));
    }

    public boolean canTransition(Status from, Status to) {
        return allowedTransitions.getOrDefault(from, EnumSet.noneOf(Status.class))
                .contains(to);
    }
}
