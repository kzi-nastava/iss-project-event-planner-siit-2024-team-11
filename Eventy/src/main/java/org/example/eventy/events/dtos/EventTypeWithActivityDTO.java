package org.example.eventy.events.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.eventy.events.models.EventType;
import org.example.eventy.solutions.dtos.categories.CategoryWithIDDTO;
import org.example.eventy.solutions.models.Category;

import java.util.ArrayList;
import java.util.List;

public class EventTypeWithActivityDTO {
    private Long id;
    private String name;
    private String description;
    private List<CategoryWithIDDTO> recommendedSolutionCategories;
    @JsonProperty("isActive")
    private boolean isActive;

    public EventTypeWithActivityDTO() {

    }

    public EventTypeWithActivityDTO(Long id, String name, String description, List<CategoryWithIDDTO> recommendedSolutionCategories,
                                    boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.recommendedSolutionCategories = recommendedSolutionCategories;
        this.isActive = isActive;
    }

    public EventTypeWithActivityDTO(EventType eventType) {
        this.id = eventType.getId();
        this.name = eventType.getName();
        this.description = eventType.getDescription();
        this.recommendedSolutionCategories = new ArrayList<>();

        for(Category category : eventType.getRecommendedSolutionCategories()) {
            recommendedSolutionCategories.add(new CategoryWithIDDTO(category));
        }

        this.isActive = eventType.isActive();
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

    public List<CategoryWithIDDTO> getRecommendedSolutionCategories() {
        return recommendedSolutionCategories;
    }

    public void setRecommendedSolutionCategories(List<CategoryWithIDDTO> recommendedSolutionCategories) {
        this.recommendedSolutionCategories = recommendedSolutionCategories;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
