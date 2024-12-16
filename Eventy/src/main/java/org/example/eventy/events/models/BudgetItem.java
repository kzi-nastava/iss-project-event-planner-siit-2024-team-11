package org.example.eventy.events.models;

import jakarta.persistence.*;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Solution;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BudgetItems")
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name="category_id", referencedColumnName = "id")
    private Category category;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "solution_id", referencedColumnName = "id")
    private List<Solution> reservedItems;

    @Column(nullable = false)
    private double plannedFunds;

    public BudgetItem()
    {

    }

    public BudgetItem(Category category, double plannedFunds) {
        this.category = category;
        this.reservedItems = new ArrayList<>();
        this.plannedFunds = plannedFunds;
    }

    public BudgetItem(Category category, double plannedFunds, List<Solution> reservedItems) {
        this.category = category;
        this.reservedItems = reservedItems;
        this.plannedFunds = plannedFunds;
    }

    public BudgetItem(List<Solution> reservedItems, double plannedFunds) {
        this.reservedItems = reservedItems;
        this.plannedFunds = plannedFunds;
    }


    public boolean isOverbudget() {
        double totalSum = 0.0;
        for (Solution solution : reservedItems) {
            totalSum += solution.getPrice();
        }
        return totalSum >= plannedFunds;
    }

    public double getRemainingFunds() {
        double totalSum = 0.0;
        for (Solution solution : reservedItems) {
            totalSum += solution.getPrice();
        }
        return plannedFunds - totalSum;
    }

    public List<Solution> getReservedItems() {
        return reservedItems;
    }

    public void setReservedItems(List<Solution> reservedItems) {
        this.reservedItems = reservedItems;
    }

    public double getPlannedFunds() {
        return plannedFunds;
    }

    public void setPlannedFunds(double plannedFunds) {
        this.plannedFunds = plannedFunds;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
