package org.example.eventy.solutions.controllers;

import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.common.models.Status;
import org.example.eventy.interactions.dtos.NotificationDTO;
import org.example.eventy.interactions.model.Notification;
import org.example.eventy.interactions.model.NotificationType;
import org.example.eventy.interactions.services.NotificationService;
import org.example.eventy.solutions.dtos.categories.CreateCategoryDTO;
import org.example.eventy.solutions.dtos.categories.CategoryWithIDDTO;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.SolutionCategoryService;
import org.example.eventy.solutions.services.SolutionService;
import org.example.eventy.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/categories")

public class SolutionCategoryController {
    @Autowired
    private SolutionCategoryService service;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PreAuthorize("hasRole('Admin') or hasRole('Provider')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWithIDDTO> createCategory(@RequestBody CreateCategoryDTO newCategory) {
        CategoryWithIDDTO createdCategory = service.createCategory(newCategory);
        if (createdCategory.getStatus().equals(Status.PENDING)) {
            sendNotification(createdCategory);
        }
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    private void sendNotification(CategoryWithIDDTO newCategory) {
        NotificationType type = NotificationType.NEW_CATEGORY_SUGGESTION;
        Long redirectionId = 5L; // admin ID
        String title = "NEW Category Suggestion!";
        Long ownerId = 5L; // admin ID
        User grader = null;
        Integer grade = null;
        String message = "A new request for a category with the name \"" + newCategory.getName() + "\" has been submitted. Tap to see more!";

        LocalDateTime timestamp = LocalDateTime.now();
        Notification notification = new Notification(type, redirectionId, title, message, grader, grade, timestamp);

        notificationService.saveNotification(ownerId, notification);
        sendNotificationToWeb(ownerId, notification);
        sendNotificationToMobile(ownerId, notification);
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
            for (Long providerId : providersToNotify) {
                sendNotification(providerId, categoryToChange, updateCategory);
            }
        }

        Category updatedCategory = service.updateCategory(updateCategory);
        if (updatedCategory == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new CategoryWithIDDTO(updatedCategory), HttpStatus.OK);
    }

    private void sendNotification(Long providerId, Category oldCategory, CategoryWithIDDTO newCategory) {
        NotificationType type = NotificationType.CATEGORY_UPDATED;
        Long redirectionId = providerId;
        String title = "Existing Category UPDATED!";
        Long ownerId = providerId;
        String message = null;
        User grader = null;
        Integer grade = null;

        String oldCategoryName = oldCategory.getName();
        String newCategoryName = newCategory.getName();
        String oldCategoryDescription = oldCategory.getDescription();
        String newCategoryDescription = newCategory.getDescription();

        if (!oldCategoryName.equals(newCategoryName) && !oldCategoryDescription.equals(newCategoryDescription)) {
            message = "The category \"" + oldCategoryName + "\" was changed to \"" + newCategoryName + "\". " +
                      "New description: \"" + newCategoryDescription + "\".";
        }

        if (!oldCategoryName.equals(newCategoryName) && oldCategoryDescription.equals(newCategoryDescription)) {
            message = "The category \"" + oldCategoryName + "\" was changed to \"" + newCategoryName + "\". ";
        }

        if (oldCategoryName.equals(newCategoryName) && !oldCategoryDescription.equals(newCategoryDescription)) {
            message = "The category \"" + oldCategoryName + "\" has a new description: \"" + newCategoryDescription + "\". ";
        }

        LocalDateTime timestamp = LocalDateTime.now();
        Notification notification = new Notification(type, redirectionId, title, message, grader, grade, timestamp);

        notificationService.saveNotification(ownerId, notification);
        sendNotificationToWeb(ownerId, notification);
        sendNotificationToMobile(ownerId, notification);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/requests/accept/{requestId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWithIDDTO> acceptRequest(@PathVariable long requestId) {
        Category pendingCategory = service.getCategory(requestId);
        String oldName = pendingCategory.getName();
        Solution changedSolution = solutionService.findSolutionWithPendingCategory(pendingCategory);

        Category acceptedCategory = service.acceptCategory(requestId);
        if (acceptedCategory == null) {
            // not found or isn't already pending
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (changedSolution != null) {
            Long providerIdToNotify = changedSolution.getProvider().getId();
            sendNotification(providerIdToNotify, oldName, changedSolution, 1);
        }
        return new ResponseEntity<>(new CategoryWithIDDTO(acceptedCategory), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value="/requests/change",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWithIDDTO> changeRequest(@RequestBody CategoryWithIDDTO changedCategory) {
        Category pendingCategory = service.getCategory(changedCategory.getId());
        String oldName = pendingCategory.getName();
        Solution changedSolution = solutionService.findSolutionWithPendingCategory(pendingCategory);

        Category updatedCategory = service.changeCategory(changedCategory);
        if (updatedCategory == null) {
            // not found or isn't already pending
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (changedSolution != null) {
            Long providerIdToNotify = changedSolution.getProvider().getId();
            sendNotification(providerIdToNotify, oldName, changedSolution, 2);
        }
        return new ResponseEntity<>(new CategoryWithIDDTO(updatedCategory), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value="/requests/replace",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> replaceRequest(@RequestParam (required = true) Long replacedCategoryId,
                                                           @RequestParam (required = true) Long newlyUsedCategoryId) {
        Category replacedCategory = service.getCategory(replacedCategoryId);
        String oldName = replacedCategory.getName();
        if (replacedCategory == null || replacedCategory.getStatus() != Status.PENDING) {
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
        Solution changedSolution = solutionService.findSolutionWithPendingCategory(replacedCategory);

        Category newCategory = service.getCategory(newlyUsedCategoryId);
        boolean successfullyReplaced = solutionService.replaceCategoryForSolutionsWithOldCategory(replacedCategory, newCategory);

        if (successfullyReplaced && changedSolution != null) {
            Long providerIdToNotify = changedSolution.getProvider().getId();
            sendNotification(providerIdToNotify, oldName, changedSolution, 3);
        }

        service.deleteCategory(replacedCategoryId);

        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    private void sendNotification(Long providerIdToNotify, String oldCategoryName, Solution changedSolution, int decision) {
        NotificationType type = null;
        String title = null;
        String message = null;
        Long redirectionId = changedSolution.getId();
        Long ownerId = providerIdToNotify;
        User grader = null;
        Integer grade = null;

        if (decision == 1) {
            // accepted
            type = NotificationType.CATEGORY_SUGGESTION_ACCEPTED;
            if (changedSolution instanceof Service) {
                title = "Service Category Suggestion ACCEPTED!";
                message = "Your request for the new service category \"" + oldCategoryName + "\" is ACCEPTED. " +
                          "Your service is now active! Tap to see more!";
            } else {
                title = "Product Category Suggestion ACCEPTED!";
                message = "Your request for the new product category \"" + oldCategoryName + "\" is ACCEPTED. " +
                          "Your product is now active! Tap to see more!";
            }

        } else if (decision == 2) {
            // changed
            type = NotificationType.CATEGORY_SUGGESTION_CHANGED;
            if (changedSolution instanceof Service) {
                title = "Service Category Suggestion UPDATED!";
                message = "Your request for the new service category \"" + oldCategoryName + "\" was not accepted " +
                          "but has instead been UPDATED with new values. Your service is now active! Tap to see more!";
            } else {
                title = "Product Category Suggestion UPDATED!";
                message = "Your request for the new product category \"" + oldCategoryName + "\" was not accepted " +
                          "but has instead been UPDATED with new values. Your product is now active! Tap to see more!";
            }

        } else {
            // replaced
            type = NotificationType.CATEGORY_SUGGESTION_REPLACED;
            if (changedSolution instanceof Service) {
                title = "Service Category Suggestion REPLACED!";
                message = "Your request for the new service category \"" + oldCategoryName + "\" was not accepted " +
                          "but has instead been REPLACED with an existing category. Your service is now active! Tap to see more!";
            } else {
                title = "Product Category Suggestion REPLACED!";
                message = "Your request for the new product category \"" + oldCategoryName + "\" was not accepted " +
                          "but has instead been REPLACED with an existing category. Your product is now active! Tap to see more!";
            }
        }

        LocalDateTime timestamp = LocalDateTime.now();
        Notification notification = new Notification(type, redirectionId, title, message, grader, grade, timestamp);

        notificationService.saveNotification(ownerId, notification);
        sendNotificationToWeb(ownerId, notification);
        sendNotificationToMobile(ownerId, notification);
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

    private void sendNotificationToWeb(Long userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/web/" + userId, new NotificationDTO(notification));
    }

    private void sendNotificationToMobile(Long userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/mobile/" + userId, new NotificationDTO(notification));
    }
}
