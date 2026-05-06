package com.example.demo.service;

import com.example.demo.domain.SubCategory;
import com.example.demo.dto.SubCategoryDto;

import java.util.List;

public interface SubCategoryService {
    List<SubCategoryDto> getAllSubCategories();
    SubCategoryDto saveSubCategory(SubCategoryDto subCategoryDto);
    SubCategoryDto getById(Long id);
    SubCategoryDto updateSubCategory(Long id, SubCategoryDto updatedSubCategory);
    boolean deleteSubCategory(Long id);
    List<SubCategoryDto> getSubCategoriesByCategoryId(Long categoryId);
}
