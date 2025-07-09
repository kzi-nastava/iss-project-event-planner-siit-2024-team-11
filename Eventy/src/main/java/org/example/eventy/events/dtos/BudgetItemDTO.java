package org.example.eventy.events.dtos;

import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.dtos.SolutionDetailsDTO;
import org.example.eventy.solutions.dtos.SolutionHistoryDTO;
import org.example.eventy.solutions.services.SolutionService;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class BudgetItemDTO {
    private Long id;
    private String category;
    private double plannedFunds;
    private List<SolutionHistoryDTO> budgetedEntries;

    public BudgetItemDTO() {}

    public BudgetItemDTO(BudgetItem budgetItem, UserService userService) {
        this.id = budgetItem.getId();
        this.category = budgetItem.getCategory().getName();
        this.plannedFunds = budgetItem.getPlannedFunds();
        this.budgetedEntries = new ArrayList<>();
        budgetItem.getReservedItems().forEach(v -> budgetedEntries.add(new SolutionHistoryDTO(v, userService)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<SolutionHistoryDTO> getBudgetedEntries() {
        return budgetedEntries;
    }

    public void setBudgetedEntries(List<SolutionHistoryDTO> budgetedEntries) {
        this.budgetedEntries = budgetedEntries;
    }
}
