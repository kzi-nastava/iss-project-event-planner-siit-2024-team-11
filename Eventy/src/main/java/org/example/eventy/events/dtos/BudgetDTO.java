package org.example.eventy.events.dtos;

import org.example.eventy.events.models.Budget;

import java.util.ArrayList;
import java.util.List;

public class BudgetDTO
{
    private List<BudgetItemDTO> items;

    public BudgetDTO() {}

    public BudgetDTO(Budget budget) {
        this.items = new ArrayList<>();
        budget.getBudgetedItems().forEach(v -> this.items.add(new BudgetItemDTO(v)));
    }
}
