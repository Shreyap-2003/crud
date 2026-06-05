package com.example.demo.service.impl;

import com.example.demo.domain.Category;
import com.example.demo.domain.SubCategory;
import com.example.demo.dto.SubCategoryDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.SubCategoryMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.SubCategoryRepository;
import com.example.demo.service.SubCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryMapper subCategoryMapper;

    @Override
    public List<SubCategoryDto> getAllSubCategories() {
        return subCategoryMapper.toDtoList(subCategoryRepository.findAll());
    }

    @Override
    public SubCategoryDto saveSubCategory(SubCategoryDto dto) {

        // DTO → Entity
        SubCategory subCategory = subCategoryMapper.toEntity(dto);

        subCategory.setCreatedDate(LocalDateTime.now()); // business logic

        // handle relationship (Category)
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        subCategory.setCategory(category);

        // save + return DTO
        return subCategoryMapper.toDto(subCategoryRepository.save(subCategory));
    }

    @Override
    public SubCategoryDto getById(Long id) {

        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("SubCategory not found with id: " + id ));

        return subCategoryMapper.toDto(subCategory);
    }
    @Override
    public SubCategoryDto updateSubCategory(Long id, SubCategoryDto dto) {

        SubCategory existing = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found with id: " + id));

        // update basic fields via MapStruct
        subCategoryMapper.updateFromDto(dto, existing);

        // handle relationship (Category)
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        existing.setCategory(category);

        return subCategoryMapper.toDto(subCategoryRepository.save(existing));
    }

    @Override
    public boolean deleteSubCategory(Long id) {

        if (!subCategoryRepository.existsById(id)) {

            throw new ResourceNotFoundException("SubCategory not found with id: " + id);
        }

        subCategoryRepository.deleteById(id);

        return true;
    }

    @Override
    public List<SubCategoryDto> getSubCategoriesByCategoryId(Long categoryId) {

        return subCategoryMapper.toDtoList(subCategoryRepository.findByCategoryId(categoryId));
    }
}