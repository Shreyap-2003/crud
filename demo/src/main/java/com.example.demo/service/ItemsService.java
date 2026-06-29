package com.example.demo.service;

import com.example.demo.dto.ItemsDto;

import java.util.List;

public interface ItemsService {

    ItemsDto saveItems(ItemsDto itemsDto);

    List<ItemsDto> getAllItems();

    ItemsDto getById(Long id);

    ItemsDto updateItems(Long id, ItemsDto itemsDto);

    void deleteItems(Long id);

    List<ItemsDto> getBySubCategoryId(Long subCategoryId);
}