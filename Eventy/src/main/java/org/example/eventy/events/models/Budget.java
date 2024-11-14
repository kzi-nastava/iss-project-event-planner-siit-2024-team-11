package org.example.eventy.events.models;

import java.util.ArrayList;

public class Budget {
    private ArrayList<BudgetItem> budgetedItems;

    public Budget() {
        this.budgetedItems = new ArrayList<>();
    }

    public Budget(ArrayList<BudgetItem> budgetedItems) {
        this.budgetedItems = budgetedItems;
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
