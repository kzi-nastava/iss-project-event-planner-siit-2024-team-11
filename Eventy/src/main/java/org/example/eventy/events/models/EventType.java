package org.example.eventy.events.models;

import org.example.eventy.solutions.models.Category;

import java.util.List;

public class EventType {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
    private List<Category> recommendedSolutionCategories;

    public EventType() {

    }

    public EventType(Long id, String name, String description, boolean isActive, List<Category> recommendedSolutionCategories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.recommendedSolutionCategories = recommendedSolutionCategories;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Category> getRecommendedSolutionCategories() {
        return recommendedSolutionCategories;
    }

    public void setRecommendedSolutionCategories(List<Category> recommendedSolutionCategories) {
        this.recommendedSolutionCategories = recommendedSolutionCategories;
    }
}
