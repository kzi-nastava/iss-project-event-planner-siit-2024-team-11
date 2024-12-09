package org.example.eventy.solutions.controllers;

import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.solutions.dtos.categories.CreateCategoryDTO;
import org.example.eventy.solutions.dtos.categories.CategoryWithIDDTO;
import org.example.eventy.solutions.services.SolutionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/categories")
public class SolutionCategoryController {

    @Autowired
    private SolutionCategoryService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWithIDDTO> createCategory(@RequestBody CreateCategoryDTO newCategory) {
        CategoryWithIDDTO createdCategory = service.createCategory(newCategory);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<CategoryWithIDDTO>> getAcceptedCategories(Pageable pageable) {
        Page<CategoryWithIDDTO> categories = service.getAcceptedCategories(pageable);
        long count = service.getAcceptedCategoryCount();
        PagedResponse<CategoryWithIDDTO> pagedCategoriesDTO = new PagedResponse<>(categories.getContent(), (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<>(pagedCategoriesDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<CategoryWithIDDTO>> getCategoryRequests(Pageable pageable) {
        Page<CategoryWithIDDTO> categories = service.getCategoryRequests(pageable);
        long count = service.getCategoryRequestCount();
        PagedResponse<CategoryWithIDDTO> pagedCategoriesDTO = new PagedResponse<>(categories.getContent(), (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<>(pagedCategoriesDTO, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWithIDDTO> updateCategory(@RequestBody CategoryWithIDDTO updateCategory) {
        CategoryWithIDDTO updatedCategory = service.updateCategory(updateCategory);
        if (updatedCategory == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        boolean success = service.deleteCategory(categoryId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
