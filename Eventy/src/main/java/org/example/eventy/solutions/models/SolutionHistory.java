package org.example.eventy.solutions.models;

import jakarta.persistence.*;

@Entity
@Table(name = "SolutionHistory")
public class SolutionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long solutionId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int discount;

    public SolutionHistory() {}

    public SolutionHistory(Long id, Long solutionId, String name, String description, Double price, int discount) {
        this.id = id;
        this.solutionId = solutionId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
    }

    public SolutionHistory(Solution solution) {
        this.solutionId = solution.getId();
        this.name = solution.getName();
        this.description = solution.getDescription();
        this.price = solution.getPrice();
        this.discount = solution.getDiscount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(Long solutionId) {
        this.solutionId = solutionId;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
