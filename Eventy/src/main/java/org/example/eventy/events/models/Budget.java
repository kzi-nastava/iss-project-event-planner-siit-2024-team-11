package org.example.eventy.events.models;

import java.util.ArrayList;

public class Budget {
    private ArrayList<BudgetItem> budgetedItems;
    private Event event;

    public Budget() {
        this.budgetedItems = new ArrayList<>();
    }

    public Budget(ArrayList<BudgetItem> budgetedItems, Event event) {
        this.budgetedItems = budgetedItems;
        this.event = event;
    }

    public boolean isOverbudget() {
        return budgetedItems.stream().anyMatch(BudgetItem::isOverbudget);
    }

    public ArrayList<BudgetItem> getBudgetedItems() {
        return budgetedItems;
    }

    public void setBudgetedItems(ArrayList<BudgetItem> budgetedItems) {
        this.budgetedItems = budgetedItems;
    }
}
