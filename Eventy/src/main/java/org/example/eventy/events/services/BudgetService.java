package org.example.eventy.events.services;

import org.example.eventy.events.dtos.BudgetDTO;
import org.example.eventy.events.dtos.BudgetItemUpdateDTO;
import org.example.eventy.events.dtos.BudgetUpdateDTO;
import org.example.eventy.events.models.Budget;
import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.repositories.BudgetRepository;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private EventService eventService;

    private final double plannedFundDefault = 100000;
    @Autowired
    private SolutionService solutionService;

    public BudgetDTO createBudget(Long eventId) {
        Budget budget = new Budget();
        Event relatedEvent = eventService.getEvent(eventId);

        List<BudgetItem> items = new ArrayList<>();
        for (Category category : relatedEvent.getType().getRecommendedSolutionCategories())
        {
            items.add(new BudgetItem(category, plannedFundDefault));
        }

        budget.setEvent(relatedEvent);
        Budget createdBudget = budgetRepository.save(budget);
        return new BudgetDTO(createdBudget);
    }

    public BudgetDTO getBudget(Long eventId) {
        Optional<Budget> budget = budgetRepository.findByEventId(eventId);
        return budget.isEmpty() ? null : new BudgetDTO(budget.get());
    }

    public BudgetDTO updateBudget(Long eventId, BudgetUpdateDTO budgetDTO) {
        Optional<Budget> budget = budgetRepository.findByEventId(eventId);
        Budget newBudget = budget.isPresent() ? budget.get() : new Budget();
        List<BudgetItem> newBudgetedItems = new ArrayList<>();
        for (BudgetItemUpdateDTO b : budgetDTO.getItems()) {
            List<Solution> newSolutions = new ArrayList<>();
            for (Long solutionId : b.getBudgetedEntries()) {
                newSolutions.add(solutionService.getSolution(solutionId));
            }
            newBudgetedItems.add(new BudgetItem(b.getCategory(), b.getPlannedFunds(), newSolutions));
        }
        newBudget.setBudgetedItems(newBudgetedItems);
        return new BudgetDTO(budgetRepository.save(newBudget));
    }
}
