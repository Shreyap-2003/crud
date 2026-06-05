package com.example.demo.service.impl;

import com.example.demo.domain.Items;
import com.example.demo.domain.SubCategory;
import com.example.demo.dto.ItemsDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ItemsMapper;
import com.example.demo.repository.ItemsRepository;
import com.example.demo.repository.SubCategoryRepository;
import com.example.demo.service.ItemsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemsServiceImpl implements ItemsService {

    private final ItemsRepository itemsRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ItemsMapper itemsMapper;

    @Override
    public List<ItemsDto> getAllItems() {
        return itemsMapper.toDtoList(itemsRepository.findAll());
    }

    @Override
    public ItemsDto saveItems(ItemsDto itemsDto) {

        // DTO → Entity
        Items items = itemsMapper.toEntity(itemsDto);

        items.setCreatedDate(LocalDateTime.now()); // business logic

        // handle relationship manually
        SubCategory subCategory = subCategoryRepository.findById(itemsDto.getSubCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("SubCategory not found with id: "+ itemsDto.getSubCategoryId()));

        items.setSubCategory(subCategory);

        // save + return DTO
        return itemsMapper.toDto(itemsRepository.save(items));
    }

    @Override
    public ItemsDto getById(Long id) {

        Items items = itemsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item not found with id: " + id));

        return itemsMapper.toDto(items);
    }

    @Override
    public ItemsDto updateItems(Long id, ItemsDto updatedItems) {

        Items existing = itemsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item not found with id: " + id));

        // update basic fields via MapStruct
        itemsMapper.updateFromDto(updatedItems, existing);

        // handle relationship manually
        SubCategory subCategory = subCategoryRepository.findById(updatedItems.getSubCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("SubCategory not found with id: "
                                + updatedItems.getSubCategoryId()));

        existing.setSubCategory(subCategory);

        return itemsMapper.toDto(itemsRepository.save(existing));
    }

    @Override
    public void deleteItems(Long id) {

        if (!itemsRepository.existsById(id)) {

            throw new ResourceNotFoundException("Item not found with id: " + id);
        }

        itemsRepository.deleteById(id);
    }

    @Override
    public List<ItemsDto> getBySubCategoryId(Long subCategoryId) {
        return itemsMapper.toDtoList(itemsRepository.findBySubCategoryId(subCategoryId));
    }
}