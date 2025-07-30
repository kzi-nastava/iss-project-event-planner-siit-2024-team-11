package org.example.eventy.events.dtos;

import jakarta.validation.constraints.NotEmpty;
import org.example.eventy.events.models.EventType;
import org.example.eventy.solutions.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CreatedEventTypeDTO {
    @NotEmpty(message = "Event type name cannot be empty!")
    private String name;
    @NotEmpty(message = "Event type description cannot be empty!")
    private String description;
    private List<Long> recommendedSolutionCategoriesIds;

    public CreatedEventTypeDTO() {

    }

    public CreatedEventTypeDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CreatedEventTypeDTO(EventType eventType) {
        this.name = eventType.getName();
        this.description = eventType.getDescription();
        this.recommendedSolutionCategoriesIds = new ArrayList<>();

        for(Category category : eventType.getRecommendedSolutionCategories()) {
            recommendedSolutionCategoriesIds.add(category.getId());
        }
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

    public List<Long> getRecommendedSolutionCategoriesIds() {
        return recommendedSolutionCategoriesIds;
    }

    public void setRecommendedSolutionCategoriesIds(List<Long> recommendedSolutionCategories) {
        this.recommendedSolutionCategoriesIds = recommendedSolutionCategories;
    }
}
