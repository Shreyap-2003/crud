package com.example.demo.dto;

import com.example.demo.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;

    private Long customerId;

    private Long partnerId;

    private OrderStatus orderStatus;

    private Long itemId;

    LocalDateTime createdTime;

}