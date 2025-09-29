package org.example.eventy.events.services;

import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.events.repositories.BudgetItemRepository;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.models.SolutionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class BudgetItemService {
    @Autowired
    private BudgetItemRepository budgetItemRepository;

    public BudgetItem createBudgetItem(Category category, Double allocatedFunds) {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setCategory(category);
        budgetItem.setPlannedFunds(allocatedFunds);
        budgetItem.setReservedItems(new ArrayList<>());
        try {
            return budgetItemRepository.save(budgetItem);
        } catch (Exception e) {
            return null;
        }
    }

    public BudgetItem updateAllocatedFunds(Long id, Double allocatedFunds) {
        BudgetItem budgetItem = budgetItemRepository.findById(id).orElse(null);
        if (budgetItem == null) { return null; }
        if (allocatedFunds < 0) { return null; }
        budgetItem.setPlannedFunds(allocatedFunds);
        return budgetItemRepository.save(budgetItem);
    }

    public BudgetItem addBudgetItemSolution(BudgetItem budgetItem, SolutionHistory solution) {
        budgetItem.getReservedItems().add(solution);
        return budgetItemRepository.save(budgetItem);
    }

    public boolean deleteBudgetItemSolution(Long id, Long solutionHistoryId, LocalDateTime eventDate) {
        BudgetItem budgetItem = budgetItemRepository.findById(id).orElse(null);
        if (budgetItem == null) { return false; }
        int lengthBefore = budgetItem.getReservedItems().size();
        budgetItem.getReservedItems().removeIf(v -> (v.getId() == solutionHistoryId && (v.getCancellationDeadline() == null || eventDate.plusDays(v.getCancellationDeadline()).isBefore(LocalDateTime.now()))));
        budgetItemRepository.save(budgetItem);
        int lengthAfter = budgetItem.getReservedItems().size();
        return lengthBefore != lengthAfter;
    }

    @Transactional
    public boolean deleteBudgetItem(Long id) {
        BudgetItem budgetItem = budgetItemRepository.findById(id).orElse(null);
        if (budgetItem == null) { return false; }
        if (!budgetItem.getReservedItems().isEmpty()) { return false; }
        budgetItemRepository.deleteById(id);
        return true;
    }
}
