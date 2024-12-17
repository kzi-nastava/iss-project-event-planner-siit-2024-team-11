package org.example.eventy.events.dtos;

import org.example.eventy.events.models.Budget;

import java.util.ArrayList;
import java.util.List;

public class BudgetUpdateDTO {

    private List<BudgetItemUpdateDTO> items;

    public BudgetUpdateDTO() {}

    public BudgetUpdateDTO(Budget budget) {
        this.items = new ArrayList<>();
        budget.getBudgetedItems().forEach(v -> this.items.add(new BudgetItemUpdateDTO(v)));
    }

    public List<BudgetItemUpdateDTO> getItems() {
        return items;
    }

    public void setItems(List<BudgetItemUpdateDTO> items) {
        this.items = items;
    }
}
