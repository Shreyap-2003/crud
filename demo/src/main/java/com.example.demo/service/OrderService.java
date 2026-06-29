package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.records.ItemOrderSummary;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    void deleteOrder(Long id);
    OrderDto assignPartner(Long orderId, Long partnerId);
    OrderDto completeOrder(Long orderId, Long partnerId);

    Page<ItemOrderSummary> getCompletedOrdersForCustomer(Long customerId, int page, int size);

    Page<ItemOrderSummary> getCompletedOrdersForPartner(Long partnerId, int page, int size);
    Page<OrderDto> getOrdersFiltered(Long customerId, Long partnerId, String status, int page, int size);
}
