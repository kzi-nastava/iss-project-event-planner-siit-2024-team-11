package org.example.eventy.events.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "Budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "budget_item_id", referencedColumnName = "id")
    private List<BudgetItem> budgetedItems;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    public Budget() {

    }

    public Budget(ArrayList<BudgetItem> budgetedItems, Event event) {
        this.budgetedItems = budgetedItems;
        this.event = event;
    }

    public boolean isOverbudget() {
        return budgetedItems.stream().anyMatch(BudgetItem::isOverbudget);
    }

    public List<BudgetItem> getBudgetedItems() {
        return budgetedItems;
    }

    public void setBudgetedItems(List<BudgetItem> budgetedItems) {
        this.budgetedItems = budgetedItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
