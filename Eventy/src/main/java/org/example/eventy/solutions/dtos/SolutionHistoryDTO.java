package org.example.eventy.solutions.dtos;

import org.example.eventy.solutions.models.SolutionHistory;
import org.example.eventy.users.models.SolutionProvider;
import org.example.eventy.users.services.UserService;

public class SolutionHistoryDTO {
    private Long id;
    private String providerName;
    private String name;
    private String description;
    private Double price;
    private Integer discount;
    private Integer cancellationDeadline;

    public SolutionHistoryDTO() {}

    public SolutionHistoryDTO(SolutionHistory solutionHistory, UserService userService) {
        this.id = solutionHistory.getId();
        this.providerName = ((SolutionProvider)userService.get(solutionHistory.getProviderId())).getName();
        this.name = solutionHistory.getName();
        this.description = solutionHistory.getDescription();
        this.price = solutionHistory.getPrice();
        this.discount = solutionHistory.getDiscount();
        this.cancellationDeadline = solutionHistory.getCancellationDeadline();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(Integer cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }
}
