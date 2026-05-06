package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "category")
    public class Category {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "name")
        private String name;

        @Column(name = "description")
        private String description;

        @Column(name = "image_url")
        private String imageUrl;

        @CreationTimestamp
        @Column(name = "created_date")
        private LocalDateTime createdDate;

        @UpdateTimestamp
        @Column(name = "last_modified_date")
        private LocalDateTime lastModifiedDate;

        @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
        @JsonManagedReference
//        @JsonIgnoreProperties({"name", "description", "imageUrl", "createdDate", "lastModifiedDate", "categoryId"})
        // 1 to N relationship - One Category has many SubCategories
        private List<SubCategory> subCategories;
    }

