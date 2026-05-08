package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.records.ItemOrderSummary;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    void deleteOrder(Long id);
    OrderDto assignPartner(Long orderId, Long partnerId);
    OrderDto completeOrder(Long orderId, Long partnerId);

    List<ItemOrderSummary> getCompletedOrdersForCustomer(Long customerId);

    List<ItemOrderSummary> getCompletedOrdersForPartner(Long partnerId);
}
