package com.example.demo.mapper;

import com.example.demo.domain.Category;
import com.example.demo.dto.CategoryDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDto, Category> {

    CategoryDto toDto(Category category);

    List<CategoryDto> toDtoList(List<Category> categories);

    Category toEntity(CategoryDto categoryDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateCategoryFromDto(CategoryDto dto, @MappingTarget Category entity);
}
