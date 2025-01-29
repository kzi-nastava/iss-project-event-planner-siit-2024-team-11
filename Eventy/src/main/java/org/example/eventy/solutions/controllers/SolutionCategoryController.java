package org.example.eventy.solutions.controllers;

import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.common.models.Status;
import org.example.eventy.solutions.dtos.categories.CreateCategoryDTO;
import org.example.eventy.solutions.dtos.categories.CategoryWithIDDTO;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.SolutionCategoryService;
import org.example.eventy.solutions.services.SolutionService;
import org.example.eventy.users.models.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/categories")

public class SolutionCategoryController {

    @Autowired
    private SolutionCategoryService service;

    @Autowired
    private SolutionService solutionService;

    @PreAuthorize("hasRole('Admin') or hasRole('Provider')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWithIDDTO> createCategory(@RequestBody CreateCategoryDTO newCategory) {
        CategoryWithIDDTO createdCategory = service.createCategory(newCategory);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('Admin') or hasRole('Provider')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CategoryWithIDDTO>> getAcceptedCategories() {
        List<Category> categories = service.getAcceptedCategories();
        List<CategoryWithIDDTO> categoryDTOs = new ArrayList<>();
        categories.forEach(category -> categoryDTOs.add(new CategoryWithIDDTO(category)));
        return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping(value = "/paged", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<CategoryWithIDDTO>> getAcceptedCategoriesPaged(Pageable pageable) {
        Page<CategoryWithIDDTO> categories = service.getAcceptedCategories(pageable);
        long count = service.getAcceptedCategoryCount();
        PagedResponse<CategoryWithIDDTO> pagedCategoriesDTO = new PagedResponse<>(categories.getContent(), (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<>(pagedCategoriesDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping(value = "/requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<CategoryWithIDDTO>> getCategoryRequests(Pageable pageable) {
        Page<CategoryWithIDDTO> categories = service.getCategoryRequests(pageable);
        long count = service.getCategoryRequestCount();
        PagedResponse<CategoryWithIDDTO> pagedCategoriesDTO = new PagedResponse<>(categories.getContent(), (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<>(pagedCategoriesDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWithIDDTO> updateCategory(@RequestBody CategoryWithIDDTO updateCategory) {
        Category categoryToChange = service.getCategory(updateCategory.getId());
        List<Solution> changedSolutions = solutionService.findAllByCategory(categoryToChange);
        if (!changedSolutions.isEmpty()) {
            Set<Long> providersToNotify = new HashSet<Long>();
            changedSolutions.forEach(changedSolution -> providersToNotify.add(changedSolution.getProvider().getId()));
            // notify
        }


        Category updatedCategory = service.updateCategory(updateCategory);
        if (updatedCategory == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new CategoryWithIDDTO(updatedCategory), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/requests/accept/{requestId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWithIDDTO> acceptRequest(@PathVariable long requestId) {
        Solution changedSolution = solutionService.findSolutionWithPendingCategory(service.getCategory(requestId));
        if (changedSolution != null) {
            Long providerIdToNotify = changedSolution.getProvider().getId();
            // notify
        }


        Category acceptedCategory = service.acceptCategory(requestId);
        if (acceptedCategory == null) {
            // not found or isn't already pending
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new CategoryWithIDDTO(acceptedCategory), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value="/requests/change",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWithIDDTO> changeRequest(@RequestBody CategoryWithIDDTO changedCategory) {
        Solution changedSolution = solutionService.findSolutionWithPendingCategory(service.getCategory(changedCategory.getId()));
        if (changedSolution != null) {
            Long providerIdToNotify = changedSolution.getProvider().getId();
            // notify
        }

        Category updatedCategory = service.changeCategory(changedCategory);
        if (updatedCategory == null) {
            // not found or isn't already pending
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new CategoryWithIDDTO(updatedCategory), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value="/requests/replace",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> replaceRequest(@RequestParam (required = true) Long replacedCategoryId,
                                                           @RequestParam (required = true) Long newlyUsedCategoryId) {
        Category replacedCategory = service.getCategory(replacedCategoryId);
        if (replacedCategory == null || replacedCategory.getStatus() != Status.PENDING) {
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }

        Solution changedSolution = solutionService.findSolutionWithPendingCategory(replacedCategory);
        if (changedSolution != null) {
            Long providerIdToNotify = changedSolution.getProvider().getId();
            // notify
        }

        Category newCategory = service.getCategory(newlyUsedCategoryId);
        boolean successfullyReplaced = solutionService.replaceCategoryForSolutionsWithOldCategory(replacedCategory, newCategory);

        service.deleteCategory(replacedCategoryId);

        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping(value = "/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        boolean success = service.deleteCategory(categoryId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
