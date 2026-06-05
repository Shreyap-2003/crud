package com.example.demo.records;

import java.time.LocalDateTime;

public record ItemOrderSummary(

        Long id,

        Long itemId,

        String itemName,
        String imageUrl,
        Double price,

        LocalDateTime createdTime,

        LocalDateTime completedTime

) {
}