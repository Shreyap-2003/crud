package com.example.demo.rest;

import com.example.demo.dto.OrderDto;
import com.example.demo.records.ItemOrderSummary;
import com.example.demo.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderResource {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderDto orderDto) {
        log.info("Request received to create order: {}", orderDto);
        orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        log.info("Request received to fetch all orders");
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    @PutMapping("/{orderId}/assign-partner/{partnerId}")
    public ResponseEntity<OrderDto> assignPartner(
            @PathVariable Long orderId,
            @PathVariable Long partnerId) {

        log.info("Assigning partner {} to order {}", partnerId, orderId);

        return ResponseEntity.ok(
                orderService.assignPartner(orderId, partnerId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        log.info("Request received to fetch order with id: {}", id);
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable Long id,
            @RequestBody OrderDto updatedOrder) {

        log.info("Request received to update order with id: {} and data: {}", id, updatedOrder);

        return ResponseEntity.ok(
                orderService.updateOrder(id, updatedOrder)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id) {

        orderService.deleteOrder(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Order deleted successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/complete")
    public ResponseEntity<OrderDto> completeOrder(
            @PathVariable Long orderId,
            @RequestParam Long partnerId) {

        return ResponseEntity.ok(
                orderService.completeOrder(orderId, partnerId)
        );
    }
    @GetMapping("/customer/{customerId}/completed-orders")
    public ResponseEntity<List<ItemOrderSummary>>
    getCompletedOrdersForCustomer(
            @PathVariable Long customerId
    ) {

        return ResponseEntity.ok(
                orderService.getCompletedOrdersForCustomer(customerId)
        );
    }
    @GetMapping("/partner/{partnerId}/completed-orders")
    public ResponseEntity<List<ItemOrderSummary>>
    getCompletedOrdersForPartner(
            @PathVariable Long partnerId
    ) {

        return ResponseEntity.ok(
                orderService.getCompletedOrdersForPartner(partnerId)
        );
    }
}