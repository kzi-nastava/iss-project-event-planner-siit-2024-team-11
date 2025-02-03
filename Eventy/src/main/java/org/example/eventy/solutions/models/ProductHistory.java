package org.example.eventy.solutions.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ProductHistory")
public class ProductHistory  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int discount;

    public ProductHistory() {

    }

    public ProductHistory(Long id, String name, String description, double price, int discount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
    }

    public ProductHistory(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
