package org.example.eventy.solutions.models;

public class Product extends Solution {
    private ProductHistory currentProduct;

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
