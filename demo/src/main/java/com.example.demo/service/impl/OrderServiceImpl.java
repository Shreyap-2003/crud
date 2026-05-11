package com.example.demo.service.impl;

import com.example.demo.domain.Items;
import com.example.demo.domain.Order;
import com.example.demo.domain.User;
import com.example.demo.dto.OrderDto;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.UserType;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.records.ItemOrderSummary;
import com.example.demo.repository.ItemsRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DispatchService;
import com.example.demo.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemsRepository itemsRepository;
    private final OrderMapper orderMapper;
    private final DispatchService dispatchService;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderMapper.toDtoList(orderRepository.findAll());
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {

        Order order = orderMapper.toEntity(orderDto);

        // customer (mandatory)
        User customer = userRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id: " + orderDto.getCustomerId()));

        order.setCustomer(customer);

        // partner (optional)
        if (orderDto.getPartnerId() != null) {
            User partner = userRepository.findById(orderDto.getPartnerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Partner not found with id: " + orderDto.getPartnerId() ));
            order.setPartner(partner);
        }

        // item (mandatory)
        Items item = itemsRepository.findById(orderDto.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + orderDto.getItemId() ));

        order.setItem(item);
        order.setOrderStatus(OrderStatus.OPEN);
        order.setCreatedTime(LocalDateTime.now());
        // SAVE ORDER
        Order savedOrder = orderRepository.save(order);

        List<User> partners = userRepository.findByUserType(UserType.PARTNER);

        // STORE PARTNER IDS
        List<Long> partnerIds = partners.stream().map(User::getId).toList();

        // CREATE DISPATCH
        dispatchService.createDispatch(savedOrder.getId(), partnerIds);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto assignPartner(Long orderId, Long partnerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId ));
        // Business validation
        if (order.getPartner() != null) {
            throw new BusinessValidationException("Partner already assigned to this order" );
        }
        User partner = userRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found with id: " + partnerId ));
        // ASSIGN PARTNER
        order.setPartner(partner);
        // SAVE ORDER
        Order savedOrder = orderRepository.save(order);
        // ACCEPT DISPATCH
        dispatchService.acceptDispatch(savedOrder.getId(),partnerId);

        return orderMapper.toDto( orderRepository.save(order) );
    }
    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id ));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto updatedOrder) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + id ));

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
        existing.setCompletedTime( LocalDateTime.now() );
        return orderMapper.toDto(orderRepository.save(existing));
    }
    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id );
        }
        orderRepository.deleteById(id);
    }
    @Override
    public OrderDto completeOrder(Long orderId, Long partnerId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId ));

        // Check if partner is assigned
        if (order.getPartner() == null) {

            throw new BusinessValidationException("No partner assigned to this order" );
        }

        //  Check if SAME partner is completing
        if (!order.getPartner().getId().equals(partnerId)) {

            throw new BusinessValidationException("This order is assigned to a different partner" );
        }

        // Optional: prevent re-completion
        if (order.getOrderStatus() == OrderStatus.COMPLETED) {

            throw new BusinessValidationException("Order is already completed" );
        }

        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setCompletedTime(LocalDateTime.now());

        return orderMapper.toDto(orderRepository.save(order));
    }
    @Override
    public List<ItemOrderSummary> getCompletedOrdersForCustomer( Long customerId ) {

        List<Order> orders = orderRepository.findByCustomerIdAndOrderStatus(customerId, OrderStatus.COMPLETED );

        return orders.stream()
                .map(order -> new ItemOrderSummary(

                        order.getId(),
                        order.getItem().getId(),
                        order.getItem().getName(),
                        order.getCreatedTime(),
                        order.getCompletedTime()

                ))
                .toList();
    }
    @Override
    public List<ItemOrderSummary> getCompletedOrdersForPartner( Long partnerId )
    {
        List<Order> orders =
                orderRepository.findByPartnerIdAndOrderStatus(partnerId, OrderStatus.COMPLETED );

        return orders.stream()
                .map(order -> new ItemOrderSummary(

                        order.getId(),
                        order.getItem().getId(),
                        order.getItem().getName(),
                        order.getCreatedTime(),
                        order.getCompletedTime()

                ))
                .toList();
    }
}