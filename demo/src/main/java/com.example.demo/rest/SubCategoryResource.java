package com.example.demo.rest;

import com.example.demo.dto.SubCategoryDto;
import com.example.demo.service.SubCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class SubCategoryResource {

    private final SubCategoryService subCategoryService;

    @PostMapping("/subcategory")
    public ResponseEntity<Void> saveSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        subCategoryService.saveSubCategory(subCategoryDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/subcategory")
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategories() {
        List<SubCategoryDto> subCategories = subCategoryService.getAllSubCategories();
        return ResponseEntity.ok(subCategories);
    }

    @GetMapping("/subcategory/{id}")
    public ResponseEntity<SubCategoryDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.getById(id));
    }

    @PutMapping("/subcategory/{id}")
    public ResponseEntity<SubCategoryDto> updateSubCategory(
            @PathVariable Long id,
            @RequestBody SubCategoryDto updatedSubCategory) {

        return ResponseEntity.ok(
                subCategoryService.updateSubCategory(id, updatedSubCategory)
        );
    }

    @DeleteMapping("/subcategory/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {

        subCategoryService.deleteSubCategory(id);

        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/subcategory/category/{categoryId}")
    public ResponseEntity<List<SubCategoryDto>> getByCategoryId(@PathVariable Long categoryId) {
        List<SubCategoryDto> subCategories =
                subCategoryService.getSubCategoriesByCategoryId(categoryId);

        return ResponseEntity.ok(subCategories);
    }
}