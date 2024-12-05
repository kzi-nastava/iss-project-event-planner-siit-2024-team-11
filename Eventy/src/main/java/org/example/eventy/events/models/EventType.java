package org.example.eventy.events.models;

import jakarta.persistence.*;
import org.example.eventy.solutions.models.Category;

import java.util.Set;

@Entity
@Table(name = "EventTypes")
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private boolean isActive;
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "EventTypesRecommendedSolutionCategories", joinColumns = @JoinColumn(name = "event_type_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "recommended_category_id", referencedColumnName = "id"))
    private Set<Category> recommendedSolutionCategories;

    public EventType() {

    }

    public EventType(Long id, String name, String description, boolean isActive, Set<Category> recommendedSolutionCategories) {
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

    public Set<Category> getRecommendedSolutionCategories() {
        return recommendedSolutionCategories;
    }

    public void setRecommendedSolutionCategories(Set<Category> recommendedSolutionCategories) {
        this.recommendedSolutionCategories = recommendedSolutionCategories;
    }
}
