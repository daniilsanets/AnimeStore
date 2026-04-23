package sanets.dev.orderservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sanets.dev.orderservice.dto.CreateOrderRequestDTO;
import sanets.dev.orderservice.dto.CreateOrderResponseDTO;
import sanets.dev.orderservice.dto.OrderResponseDTO;
import sanets.dev.orderservice.dto.OrderStatusUpdateRequestDTO;
import sanets.dev.orderservice.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @CircuitBreaker(name = "createOrder", fallbackMethod = "createFallback")
    @PostMapping
    public ResponseEntity<CreateOrderResponseDTO> createOrder(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody CreateOrderRequestDTO request
    ) {
        var result = orderService.createOrder(request, idempotencyKey);
        var status = result.reused() ? HttpStatus.OK : HttpStatus.CREATED;
        return ResponseEntity.status(status).body(result.response());
    }

    @CircuitBreaker(name = "getOrderById", fallbackMethod = "getOrderByIdFallback")
    @GetMapping("/{orderId}")
    public OrderResponseDTO getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping("/{orderId}/status")
    public OrderResponseDTO updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequestDTO request
    ) {
        return orderService.updateOrderStatus(orderId, request);
    }

    private ResponseEntity<CreateOrderResponseDTO> createFallback(
            String idempotencyKey,
            CreateOrderRequestDTO request,
            Throwable throwable
    ) {
        String message = "Catalog service is unavailable. Please try again later.";

        if (throwable instanceof CallNotPermittedException) {
            message = "Catalog service is temporarily unavailable (circuit breaker open). Please try again later.";
        }

        if (throwable instanceof ResponseStatusException responseStatusException) {
            if (responseStatusException.getReason() != null && !responseStatusException.getReason().isBlank()) {
                message = responseStatusException.getReason();
            }
        } else if (throwable.getMessage() != null && !throwable.getMessage().isBlank()) {
            message = throwable.getMessage();
        }

        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
    }

    private OrderResponseDTO getOrderByIdFallback(Long orderId, Throwable throwable) {
        String message = "Order details are temporarily unavailable.";
        if (throwable != null && throwable.getMessage() != null && !throwable.getMessage().isBlank()) {
            message = throwable.getMessage();
        }
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
    }
}
