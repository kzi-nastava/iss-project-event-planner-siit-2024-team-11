package org.example.eventy.solutions.models;


import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Product")
public class Product extends Solution {
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "solution_history_id", referencedColumnName = "id")
    private SolutionHistory currentProduct; // we keep this if any edits are made to the current product

    ///////////////////////////////////

    public Product() {

    }

    public Product(SolutionHistory currentProduct) {
        this.currentProduct = currentProduct;
    }

    public SolutionHistory getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(SolutionHistory currentProduct) {
        this.currentProduct = currentProduct;
    }
}
