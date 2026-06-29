package com.example.demo.rest;

import com.example.demo.dto.OrderDto;
import com.example.demo.records.ItemOrderSummary;
import com.example.demo.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

//    @GetMapping
//    public ResponseEntity<List<OrderDto>> getAllOrders() {
//        log.info("Request received to fetch all orders");
//        List<OrderDto> orders = orderService.getAllOrders();
//        return ResponseEntity.ok(orders);
//    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrdersList() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long partnerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getOrdersFiltered(customerId, partnerId, status, page, size));
    }
    @PutMapping("/{id}/assign-partner/{partnerId}")
    public ResponseEntity<OrderDto> assignPartner(@PathVariable Long id, @PathVariable Long partnerId) {

        log.info("Assigning partner {} to order {}", partnerId, id);
        return ResponseEntity.ok(orderService.assignPartner(id, partnerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        log.info("Request received to fetch order with id: {}", id);
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @RequestBody OrderDto updatedOrder) {

        log.info("Request received to update order with id: {} and data: {}", id, updatedOrder);
        return ResponseEntity.ok(orderService.updateOrder(id, updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id) {

        orderService.deleteOrder(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Order deleted successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<OrderDto> completeOrder(@PathVariable Long id,@RequestParam Long partnerId) {

        return ResponseEntity.ok(orderService.completeOrder(id, partnerId));
    }
    @GetMapping("/customer/{customerId}/completed-orders")
    public ResponseEntity<Page<ItemOrderSummary>>
    getCompletedOrdersForCustomer(@PathVariable Long customerId,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getCompletedOrdersForCustomer(customerId, page, size));
    }
    @GetMapping("/partner/{partnerId}/completed-orders")
    public ResponseEntity<Page<ItemOrderSummary>> getCompletedOrdersForPartner(
            @PathVariable Long partnerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getCompletedOrdersForPartner(partnerId, page, size));
    }
}