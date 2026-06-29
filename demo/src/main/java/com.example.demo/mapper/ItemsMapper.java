package com.example.demo.mapper;

import com.example.demo.domain.Items;
import com.example.demo.dto.ItemsDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemsMapper extends EntityMapper<ItemsDto, Items> {

    @Mapping(source = "subCategory.id", target = "subCategoryId")
    ItemsDto toDto(Items items);

    @Mapping(target = "subCategory", ignore = true)
    Items toEntity(ItemsDto itemsDTO);

    List<ItemsDto> toDtoList(List<Items> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "subCategory", ignore = true)
    void updateFromDto(ItemsDto dto, @MappingTarget Items entity);
}
