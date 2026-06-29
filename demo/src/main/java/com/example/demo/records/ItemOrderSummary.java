package com.example.demo.records;

import java.time.LocalDateTime;

public record ItemOrderSummary(

        Long id,

        Long itemId,

        String itemName,
        String imageUrl,
        Double price,

        LocalDateTime createdTime,

        LocalDateTime completedTime,

         Long customerId,     // ← add
        String customerName,   // ← add
        String customerPhone,  // ← add
        String customerAddress // ← add

) {
}