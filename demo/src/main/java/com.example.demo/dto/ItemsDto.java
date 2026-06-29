package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDto {

    private Long id;

    private String name;

    private Double price;

    private String imageUrl;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    // Instead of full SubCategory object, just use ID
    private Long subCategoryId;
}