package org.example.eventy.events.dtos;

import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.solutions.dtos.SolutionCardDTO;

import java.util.ArrayList;
import java.util.List;

public class BudgetItemDTO {
    private String category;
    private double plannedFunds;
    private List<SolutionCardDTO> budgetedEntries;

    public BudgetItemDTO() {}

    public BudgetItemDTO(BudgetItem budgetItem) {
        this.category = budgetItem.getCategory().getName();
        this.plannedFunds = budgetItem.getPlannedFunds();
        this.budgetedEntries = new ArrayList<>();
        // TO-DO: pass loggedInUser here
        budgetItem.getReservedItems().forEach(v -> budgetedEntries.add(new SolutionCardDTO(v, null)));
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPlannedFunds() {
        return plannedFunds;
    }

    public void setPlannedFunds(double plannedFunds) {
        this.plannedFunds = plannedFunds;
    }

    public List<SolutionCardDTO> getBudgetedEntries() {
        return budgetedEntries;
    }

    public void setBudgetedEntries(List<SolutionCardDTO> budgetedEntries) {
        this.budgetedEntries = budgetedEntries;
    }
}
