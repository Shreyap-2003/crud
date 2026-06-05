package com.example.demo.mapper;

import com.example.demo.domain.SubCategory;
import com.example.demo.dto.SubCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper extends EntityMapper<SubCategoryDto, SubCategory> {

    @Mapping(source = "category.id", target = "categoryId")
    SubCategoryDto toDto(SubCategory subCategory);

    List<SubCategoryDto> toDtoList(List<SubCategory> subCategories);

    @Mapping(target = "category", ignore = true)
    SubCategory toEntity(SubCategoryDto subCategoryDTO);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    void updateFromDto(SubCategoryDto dto, @MappingTarget SubCategory entity);
}