package org.example.eventy.solutions.dtos;

import java.util.List;

public class PriceListDTO {
    private List<Long> solutionIds;
    private List<Double> newPrices;
    private List<Double> newDiscounts;

    public PriceListDTO() {}

    public List<Long> getSolutionIds() {
        return solutionIds;
    }

    public void setSolutionIds(List<Long> solutionIds) {
        this.solutionIds = solutionIds;
    }

    public List<Double> getNewPrices() {
        return newPrices;
    }

    public void setNewPrices(List<Double> newPrices) {
        this.newPrices = newPrices;
    }

    public List<Double> getNewDiscounts() {
        return newDiscounts;
    }

    public void setNewDiscounts(List<Double> newDiscounts) {
        this.newDiscounts = newDiscounts;
    }
}
