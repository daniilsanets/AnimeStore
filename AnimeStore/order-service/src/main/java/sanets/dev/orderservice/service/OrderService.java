package sanets.dev.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.concurrent.ConcurrentHashMap;
import sanets.dev.orderservice.client.CatalogClient;
import sanets.dev.orderservice.client.dto.CatalogProductDto;
import sanets.dev.orderservice.dto.CreateOrderItemRequestDTO;
import sanets.dev.orderservice.dto.CreateOrderRequestDTO;
import sanets.dev.orderservice.dto.CreateOrderResponseDTO;
import sanets.dev.orderservice.dto.OrderItemResponseDTO;
import sanets.dev.orderservice.dto.OrderResponseDTO;
import sanets.dev.orderservice.dto.OrderStatusUpdateRequestDTO;
import sanets.dev.orderservice.entity.Order;
import sanets.dev.orderservice.entity.OrderItem;
import sanets.dev.orderservice.entity.OrderStatusHistory;
import sanets.dev.orderservice.entity.Status;
import sanets.dev.orderservice.kafka.event.OrderCreatedEvent;
import sanets.dev.orderservice.kafka.producer.OrderProducer;
import sanets.dev.orderservice.repository.OrderItemRepository;
import sanets.dev.orderservice.repository.OrderRepository;
import sanets.dev.orderservice.repository.OrderStatusHistoryRepository;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStateMachine orderStateMachine;
    private final OrderProducer orderProducer;
    private final CatalogClient catalogClient;

    private final ConcurrentHashMap<Long, CatalogProductDto> catalogCache = new ConcurrentHashMap<>();

    @Transactional
    public CreateOrderResult createOrder(CreateOrderRequestDTO request, String idempotencyKeyHeader) {
        UUID idempotencyKey = parseIdempotencyKey(idempotencyKeyHeader);

        var existingOrder = orderRepository.findByIdempotencyKey(idempotencyKey);
        if (existingOrder.isPresent()) {
            return new CreateOrderResult(toCreateOrderResponse(existingOrder.get()), true);
        }

        var resolvedItems = resolveProductsFromCatalog(request.getOrderItems());
        var totalAmount = totalAmount(resolvedItems);

        var order = Order.builder()
                .status(Status.NEW)
                .userId(request.getUserId())
                .currency("USD")
                .idempotencyKey(idempotencyKey)
                .totalAmount(totalAmount)
                .build();

        var persistedOrder = orderRepository.save(order);

        var orderItems = resolvedItems.stream()
                .map(item -> mapToOrderItem(item, persistedOrder.getId()))
                .toList();
        orderItemRepository.saveAll(orderItems);

        saveStatusHistory(persistedOrder.getId(), Status.NEW, Status.NEW, "Order created");

        orderProducer.sendOrderCreateEvent(persistedOrder);

        return new CreateOrderResult(toCreateOrderResponse(persistedOrder), false);
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        var items = orderItemRepository.findByOrderId(orderId);
        return mapToOrderResponse(order, items);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatusUpdateRequestDTO request) {
        if (request == null || request.getNewStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "newStatus is required");
        }

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (!orderStateMachine.canTransition(order.getStatus(), request.getNewStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Invalid transition from " + order.getStatus() + " to " + request.getNewStatus()
            );
        }

        var fromStatus = order.getStatus();
        order.setStatus(request.getNewStatus());
        var savedOrder = orderRepository.save(order);
        saveStatusHistory(orderId, fromStatus, request.getNewStatus(), defaultReason(request.getReason()));

        var items = orderItemRepository.findByOrderId(orderId);
        return mapToOrderResponse(savedOrder, items);
    }

    private BigDecimal totalAmount(Set<ResolvedOrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.productPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderItem mapToOrderItem(ResolvedOrderItem item, Long orderId) {
        return OrderItem.builder()
                .orderId(orderId)
                .productId(item.productId())
                .productNameSnapshot(item.productName())
                .productPriceSnapshot(item.productPrice())
                .quantity(item.quantity())
                .lineTotal(item.productPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .build();
    }

    private OrderResponseDTO mapToOrderResponse(Order order, List<OrderItem> items) {
        Set<OrderItemResponseDTO> itemResponses = items.stream()
                .map(item -> OrderItemResponseDTO.builder()
                        .productId(item.getProductId())
                        .productNameSnapshot(item.getProductNameSnapshot())
                        .productPriceSnapshot(item.getProductPriceSnapshot())
                        .quantity(item.getQuantity())
                        .lineTotal(item.getLineTotal())
                        .build())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .createdAt(order.getCreatedAt())
                .items(itemResponses)
                .build();
    }

    private void saveStatusHistory(Long orderId, Status fromStatus, Status toStatus, String reason) {
        var statusHistory = OrderStatusHistory.builder()
                .orderId(orderId)
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .reason(reason)
                .build();
        orderStatusHistoryRepository.save(statusHistory);
    }

    private String defaultReason(String reason) {
        return reason == null || reason.isBlank() ? "Status changed manually" : reason;
    }

    private UUID parseIdempotencyKey(String idempotencyKeyHeader) {
        if (idempotencyKeyHeader == null || idempotencyKeyHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Idempotency-Key header is required");
        }
        try {
            return UUID.fromString(idempotencyKeyHeader);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Idempotency-Key must be a valid UUID");
        }
    }

    private CreateOrderResponseDTO toCreateOrderResponse(Order order) {
        return CreateOrderResponseDTO.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }

    private Set<ResolvedOrderItem> resolveProductsFromCatalog(Set<CreateOrderItemRequestDTO> requestItems) {
        return requestItems.stream()
                .map(this::resolveItem)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private ResolvedOrderItem resolveItem(CreateOrderItemRequestDTO requestItem) {
        CatalogProductDto product;
        try {
            product = catalogClient.getProductById(requestItem.getProductId());
        } catch (Exception exception) {
            product = catalogCache.get(requestItem.getProductId());
            if (product == null) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Catalog service is unavailable and no cached data exists for productId=" + requestItem.getProductId()
                );
            }
        }

        if (product.getId() == null || product.getPrice() == null || product.getName() == null || product.getActive() == null) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Catalog product data is incomplete for productId=" + requestItem.getProductId()
            );
        }

        if (Boolean.FALSE.equals(product.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Product is inactive and cannot be ordered: " + requestItem.getProductId()
            );
        }

        catalogCache.put(requestItem.getProductId(), product);

        return new ResolvedOrderItem(
                product.getId(),
                product.getName(),
                product.getPrice(),
                requestItem.getQuantity()
        );
    }

    private record ResolvedOrderItem(Long productId, String productName, BigDecimal productPrice, Integer quantity) {}
    public record CreateOrderResult(CreateOrderResponseDTO response, boolean reused) {}
}
