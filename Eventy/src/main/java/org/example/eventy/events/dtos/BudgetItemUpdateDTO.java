package org.example.eventy.events.dtos;

import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.solutions.models.Category;

import java.util.ArrayList;
import java.util.List;

public class BudgetItemUpdateDTO {
    private Category category;
    private double plannedFunds;
    private List<Long> budgetedSolutionIds;

    public BudgetItemUpdateDTO() {}

    public BudgetItemUpdateDTO(BudgetItem budgetItem) {
        this.category = budgetItem.getCategory();
        this.plannedFunds = budgetItem.getPlannedFunds();
        this.budgetedSolutionIds = new ArrayList<>();
        budgetItem.getReservedItems().forEach(v -> budgetedSolutionIds.add(v.getId()));
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPlannedFunds() {
        return plannedFunds;
    }

    public void setPlannedFunds(double plannedFunds) {
        this.plannedFunds = plannedFunds;
    }

    public List<Long> getBudgetedEntries() {
        return budgetedSolutionIds;
    }

    public void setBudgetedEntries(List<Long> budgetedEntries) {
        this.budgetedSolutionIds = budgetedEntries;
    }
}
