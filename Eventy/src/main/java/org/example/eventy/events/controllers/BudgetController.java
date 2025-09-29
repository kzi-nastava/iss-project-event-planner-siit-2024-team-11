package org.example.eventy.events.controllers;

import org.example.eventy.events.dtos.BudgetDTO;
import org.example.eventy.events.dtos.BudgetItemDTO;
import org.example.eventy.events.models.Budget;
import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.BudgetItemService;
import org.example.eventy.events.services.BudgetService;
import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.services.SolutionCategoryService;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.example.eventy.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@PreAuthorize("hasRole('Organizer')")
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;
    @Autowired
    private EventService eventService;
    @Autowired
    private BudgetItemService budgetItemService;
    @Autowired
    private SolutionCategoryService solutionCategoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BudgetDTO> getBudget(@PathVariable Long eventId, @RequestHeader(value = "Authorization", required = false) String token) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        Event event = eventService.getEvent(eventId);
        if (!event.getOrganiser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Budget budget = budgetService.getBudget(eventId);
        if (budget == null) {
            budget = budgetService.createBudget(eventId);
        }
        LocalDateTime eventDate = event.getDate();
        return new ResponseEntity<>(new BudgetDTO(budget, eventDate, userService), HttpStatus.OK);
    }

    @PostMapping(value = "/{eventId}/item/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BudgetItemDTO> createBudgetItem(@PathVariable Long eventId, @PathVariable Long categoryId, @RequestHeader(value = "Authorization") String token, @RequestBody Double allocatedFunds) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        Event event = eventService.getEvent(eventId);
        if (!event.getOrganiser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Budget budget = budgetService.getBudget(eventId);
        Category category = solutionCategoryService.getCategory(categoryId);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BudgetItem budgetItem = budgetItemService.createBudgetItem(category, allocatedFunds);
        budgetService.addBudgetItem(budget, budgetItem);

        return new ResponseEntity<>(new BudgetItemDTO(budgetItem, userService), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{eventId}/item/{budgetItemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> removeBudgetItem(@PathVariable Long eventId, @PathVariable Long budgetItemId, @RequestHeader(value = "Authorization") String token)    {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        Event event = eventService.getEvent(eventId);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!event.getOrganiser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Budget budget = budgetService.getBudget(eventId);

        budgetService.deleteBudgetItemFromBudget(budget, budgetItemId);
        if (budgetItemService.deleteBudgetItem(budgetItemId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/item/{budgetItemId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BudgetItemDTO> updateBudgetItemFunds(@PathVariable Long budgetItemId, @RequestBody Double allocatedFunds, @RequestHeader(value = "Authorization") String token) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        Budget budget = budgetService.getBudgetByBudgetItemId(budgetItemId);
        if (budget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!budget.getEvent().getOrganiser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        BudgetItem budgetItem = budgetItemService.updateAllocatedFunds(budgetItemId, allocatedFunds);
        if (budgetItem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<BudgetItemDTO>(new BudgetItemDTO(budgetItem, userService), HttpStatus.OK);
    }

    @DeleteMapping(value = "/item/{budgetItemId}/solution/{solutionHistoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> removeBudgetItemSolution(@PathVariable Long budgetItemId, @PathVariable Long solutionHistoryId, @RequestHeader(value = "Authorization") String token) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        Budget budget = budgetService.getBudgetByBudgetItemId(budgetItemId);
        if (budget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!budget.getEvent().getOrganiser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        boolean isDeleted = budgetItemService.deleteBudgetItemSolution(budgetItemId, solutionHistoryId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
