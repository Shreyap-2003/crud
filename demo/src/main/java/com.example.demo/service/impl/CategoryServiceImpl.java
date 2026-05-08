package com.example.demo.service.impl;

import com.example.demo.domain.Category;
import com.example.demo.dto.CategoryDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        return categoryMapper.toDtoList(categories);
    }

    @Override
    public void saveCategory(CategoryDto categoryDto) {

        Category category = categoryMapper.toEntity(categoryDto);

        category.setCreatedDate(LocalDateTime.now());

        categoryRepository.save(category);
    }

    @Override
    public CategoryDto getById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        ));

        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto updatedCategory) {

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        ));

        categoryMapper.updateCategoryFromDto(updatedCategory, existing);

        Category savedCategory = categoryRepository.save(existing);

        return categoryMapper.toDto(savedCategory);
    }
    @Override
    public void deleteCategory(Long id) {

        if (!categoryRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "Category not found with id: " + id
            );
        }

        categoryRepository.deleteById(id);
    }
}