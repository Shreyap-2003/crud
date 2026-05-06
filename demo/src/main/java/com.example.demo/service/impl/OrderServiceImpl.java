package com.example.demo.service.impl;

import com.example.demo.domain.Items;
import com.example.demo.domain.Order;
import com.example.demo.domain.User;
import com.example.demo.dto.OrderDto;
import com.example.demo.enums.OrderStatus;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.ItemsRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemsRepository itemsRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderMapper.toDtoList(orderRepository.findAll());
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        Order order = orderMapper.toEntity(orderDto);

        // customer (mandatory)
        User customer = userRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Customer not found with id: " + orderDto.getCustomerId()
                        ));

        order.setCustomer(customer);

        // partner (optional)
        if (orderDto.getPartnerId() != null) {
            User partner = userRepository.findById(orderDto.getPartnerId())
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Partner not found with id: " + orderDto.getPartnerId()
                            ));
            order.setPartner(partner);
        }

        // item (mandatory)
        Items item = itemsRepository.findById(orderDto.getItemId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Item not found with id: " + orderDto.getItemId()
                        ));

        order.setItem(item);

        return orderMapper.toDto(orderRepository.save(order));
    }
    @Override
    public OrderDto assignPartner(Long orderId, Long partnerId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 🔥 CHECK if already assigned
        if (order.getPartner() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Partner already assigned to this order"
            );
        }

        User partner = userRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        order.setPartner(partner);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Order not found with id: " + id
                        ));

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto updatedOrder) {

        Order existing = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Order not found with id: " + id
                        ));

        // update basic fields
        orderMapper.updateFromDto(updatedOrder, existing);

// handle relationships manually
        if (updatedOrder.getCustomerId() != null) {
            User customer = userRepository.findById(updatedOrder.getCustomerId()).orElseThrow();
            existing.setCustomer(customer);
        }

        if (updatedOrder.getPartnerId() != null) {
            User partner = userRepository.findById(updatedOrder.getPartnerId()).orElseThrow();
            existing.setPartner(partner);
        }

        if (updatedOrder.getItemId() != null) {
            Items item = itemsRepository.findById(updatedOrder.getItemId()).orElseThrow();
            existing.setItem(item);
        }

        return orderMapper.toDto(orderRepository.save(existing));
    }

    @Override
    public void deleteOrder(Long id) {

        if (!orderRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order not found with id: " + id
            );
        }

        orderRepository.deleteById(id);
    }
    @Override
    public OrderDto completeOrder(Long orderId, Long partnerId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Order not found with id: " + orderId
                        ));

        // Check if partner is assigned
        if (order.getPartner() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No partner assigned to this order"
            );
        }

        //  Check if SAME partner is completing
        if (!order.getPartner().getId().equals(partnerId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This order is assigned to a different partner"
            );
        }

        //  Optional: prevent re-completion
        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Order is already completed"
            );
        }

        order.setOrderStatus(OrderStatus.COMPLETED);

        return orderMapper.toDto(orderRepository.save(order));
    }
}