package com.example.demo.repository;

import com.example.demo.domain.Items;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Items, Long> {

    // Get all items by subcategory
    List<Items> findBySubCategoryId(Long subCategoryId);
}