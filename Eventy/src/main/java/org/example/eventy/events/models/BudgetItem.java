package org.example.eventy.events.models;

import org.example.eventy.solutions.models.Solution;

import java.util.ArrayList;

public class BudgetItem {
    private ArrayList<Solution> reservedItems;
    private double plannedFunds;

    public BudgetItem()
    {

    }

    public BudgetItem(ArrayList<Solution> reservedItems, double plannedFunds) {
        this.reservedItems = reservedItems;
        this.plannedFunds = plannedFunds;
    }

    public BudgetItem(double plannedFunds) {
        this.plannedFunds = plannedFunds;
        this.reservedItems = new ArrayList<>();
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

    public ArrayList<Solution> getReservedItems() {
        return reservedItems;
    }

    public void setReservedItems(ArrayList<Solution> reservedItems) {
        this.reservedItems = reservedItems;
    }

    public double getPlannedFunds() {
        return plannedFunds;
    }

    public void setPlannedFunds(double plannedFunds) {
        this.plannedFunds = plannedFunds;
    }
}
