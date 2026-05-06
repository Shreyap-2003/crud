package com.example.demo.service;



import com.example.demo.domain.Category;
import com.example.demo.domain.SubCategory;
import com.example.demo.dto.CategoryDto;

import java.util.List;

    public interface CategoryService {
        List<CategoryDto> getAllCategories();
        void saveCategory(CategoryDto categoryDto);
        CategoryDto getById(Long id);
        CategoryDto updateCategory(Long id, CategoryDto updatedCategory);
        void deleteCategory(Long id);
    }

