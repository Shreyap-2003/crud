package com.example.demo.repository;

import com.example.demo.domain.Order;
import com.example.demo.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerIdAndOrderStatus(
            Long customerId,
            OrderStatus orderStatus,
            Pageable pageable
    );
    Page<Order> findByPartnerIdAndOrderStatus(
            Long partnerId,
            OrderStatus orderStatus,
            Pageable pageable
    );
    @Query("SELECT o FROM Order o WHERE " +
            "(:customerId IS NULL OR o.customer.id = :customerId) AND " +
            "(:partnerId IS NULL OR o.partner.id = :partnerId) AND " +
            "(:status IS NULL OR " +
            "(:status = 'ACTIVE' AND o.orderStatus IN (com.example.demo.enums.OrderStatus.OPEN, com.example.demo.enums.OrderStatus.IN_PROGRESS)) OR " +
            "(:status = 'OPEN' AND o.orderStatus = com.example.demo.enums.OrderStatus.OPEN) OR " +
            "(:status = 'COMPLETED' AND o.orderStatus = com.example.demo.enums.OrderStatus.COMPLETED))")
    Page<Order> findByFilters(
            @Param("customerId") Long customerId,
            @Param("partnerId") Long partnerId,
            @Param("status") String status,
            Pageable pageable);
    }