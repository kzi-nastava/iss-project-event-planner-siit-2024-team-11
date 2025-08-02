package org.example.eventy.events.services;

import org.example.eventy.events.dtos.BudgetDTO;
import org.example.eventy.events.dtos.BudgetItemUpdateDTO;
import org.example.eventy.events.dtos.BudgetUpdateDTO;
import org.example.eventy.events.models.Budget;
import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.repositories.BudgetItemRepository;
import org.example.eventy.events.repositories.BudgetRepository;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private BudgetItemService budgetItemService;
    @Autowired
    private EventService eventService;

    private final double plannedFundDefault = 200;

    public Budget createBudget(Long eventId) {
        Budget budget = new Budget();
        Event relatedEvent = eventService.getEvent(eventId);

        List<BudgetItem> items = new ArrayList<>();
        for (Category category : relatedEvent.getType().getRecommendedSolutionCategories())
        {
            items.add(budgetItemService.createBudgetItem(category, plannedFundDefault));
        }

        budget.setEvent(relatedEvent);
        budget.setBudgetedItems(items);
        return budgetRepository.save(budget);
    }

    public Budget getBudget(Long eventId) {
        return budgetRepository.findByEventId(eventId).orElse(null);
    }

    public Budget addBudgetItem(Budget budget, BudgetItem budgetItem) {
        budget.getBudgetedItems().add(budgetItem);
        return budgetRepository.save(budget);
    }

    public boolean deleteBudgetItemFromBudget(Budget budget, Long budgetItemId) {
        List<BudgetItem> items = budget.getBudgetedItems();
        items.removeIf(v -> v.getId().equals(budgetItemId));
        budgetRepository.save(budget);
        return true;
    }
}
