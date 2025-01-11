package org.example.eventy.solutions.models;


import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Product")
public class Product extends Solution {
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_history_id", referencedColumnName = "id")
    private ProductHistory currentProduct; // we keep this if any edits are made to the current product

    ///////////////////////////////////

    public Product() {

    }

    public Product(ProductHistory currentProduct) {
        this.currentProduct = currentProduct;
    }

    public ProductHistory getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(ProductHistory currentProduct) {
        this.currentProduct = currentProduct;
    }
}
