package org.example.eventy.events.dtos;

import org.example.eventy.events.models.Budget;
import org.example.eventy.solutions.services.SolutionService;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BudgetDTO
{
    private List<BudgetItemDTO> categoryItems;
    private LocalDateTime eventDate;

    public BudgetDTO() {}

    public BudgetDTO(Budget budget, LocalDateTime eventDate, UserService userService) {
        this.eventDate = eventDate;
        this.categoryItems = new ArrayList<>();
        budget.getBudgetedItems().forEach(v -> this.categoryItems.add(new BudgetItemDTO(v, userService)));
    }

    public List<BudgetItemDTO> getCategoryItems() {
        return categoryItems;
    }

    public void setCategoryItems(List<BudgetItemDTO> categoryItems) {
        this.categoryItems = categoryItems;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }
}
