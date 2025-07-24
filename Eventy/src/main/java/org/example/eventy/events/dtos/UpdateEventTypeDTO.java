package org.example.eventy.events.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.eventy.events.models.EventType;
import org.example.eventy.solutions.models.Category;

import java.util.ArrayList;
import java.util.List;

public class UpdateEventTypeDTO {
    @NotNull(message = "Id cannot be null")
    private Long id;
    @NotEmpty(message = "Event type name cannot be empty!")
    private String name;
    @NotEmpty(message = "Event type description cannot be empty!")
    private String description;
    private List<Long> recommendedSolutionCategoriesIds;

    public UpdateEventTypeDTO() {

    }

    public UpdateEventTypeDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public UpdateEventTypeDTO(EventType eventType) {
        this.id = eventType.getId();
        this.name = eventType.getName();
        this.description = eventType.getDescription();
        this.recommendedSolutionCategoriesIds = new ArrayList<>();

        for(Category category : eventType.getRecommendedSolutionCategories()) {
            recommendedSolutionCategoriesIds.add(category.getId());
        }
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

    public List<Long> getRecommendedSolutionCategoriesIds() {
        return recommendedSolutionCategoriesIds;
    }

    public void setRecommendedSolutionCategoriesIds(List<Long> recommendedSolutionCategories) {
        this.recommendedSolutionCategoriesIds = recommendedSolutionCategories;
    }
}
