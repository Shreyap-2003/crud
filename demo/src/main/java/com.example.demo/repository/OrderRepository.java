package com.example.demo.repository;

import com.example.demo.domain.Order;
import com.example.demo.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdAndOrderStatus(
            Long customerId,
            OrderStatus orderStatus
    );

    List<Order> findByPartnerIdAndOrderStatus(
            Long partnerId,
            OrderStatus orderStatus
    );


}