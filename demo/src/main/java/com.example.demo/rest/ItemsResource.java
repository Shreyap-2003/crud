package com.example.demo.rest;

import com.example.demo.dto.ItemsDto;
import com.example.demo.service.ItemsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/items")
public class ItemsResource {

    private final ItemsService itemsService;

    @PostMapping
    public ResponseEntity<Void> saveItems(@RequestBody ItemsDto itemsDto) {
        log.info("Request received to create item: {}", itemsDto);
        itemsService.saveItems(itemsDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ItemsDto>> getAllItems() {
        log.info("Request received to fetch all items");
        List<ItemsDto> items = itemsService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemsDto> getById(@PathVariable Long id) {
        log.info("Request received to fetch item with id: {}", id);
        return ResponseEntity.ok(itemsService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemsDto> updateItems(
            @PathVariable Long id,
            @RequestBody ItemsDto updatedItems) {
        log.info("Request received to update item with id: {} and data: {}", id, updatedItems);

        return ResponseEntity.ok(
                itemsService.updateItems(id, updatedItems)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {

        log.info("Request received to delete item with id: {}", id);
        itemsService.deleteItems(id);

        return ResponseEntity.ok("Item deleted successfully");
    }

    @GetMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<List<ItemsDto>> getBySubCategoryId(@PathVariable Long subCategoryId) {

        log.info("Request received to fetch items by subCategoryId: {}", subCategoryId);
        List<ItemsDto> items = itemsService.getBySubCategoryId(subCategoryId);

        return ResponseEntity.ok(items);
    }
}