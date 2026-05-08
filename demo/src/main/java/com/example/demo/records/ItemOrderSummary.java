package com.example.demo.records;

import java.time.LocalDateTime;

public record ItemOrderSummary(

        Long orderId,

        Long itemId,

        String itemName,

        LocalDateTime createdTime,

        LocalDateTime completedTime

) {
}