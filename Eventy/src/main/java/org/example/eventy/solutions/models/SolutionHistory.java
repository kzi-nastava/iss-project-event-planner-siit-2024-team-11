package org.example.eventy.solutions.models;

import jakarta.persistence.*;

@Entity
@Table(name = "SolutionHistory")
public class SolutionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int discount;

    @Column(nullable = true)
    private Integer cancellationDeadline;

    public SolutionHistory() {}

    public SolutionHistory(Long id, Long providerId, String name, String description, Double price, int discount, int cancellationDeadline) {
        this.id = id;
        this.providerId = providerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.cancellationDeadline = cancellationDeadline;
    }

    public SolutionHistory(Solution solution) {
        this.providerId = solution.getProvider().getId();
        this.name = solution.getName();
        this.description = solution.getDescription();
        this.price = solution.getPrice();
        this.discount = solution.getDiscount();
        this.cancellationDeadline = solution instanceof Service ? ((Service) solution).getCancellationDeadline() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
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

    public Integer getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(Integer cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }
}
