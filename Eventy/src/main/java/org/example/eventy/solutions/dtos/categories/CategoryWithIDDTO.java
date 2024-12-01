package org.example.eventy.solutions.dtos.categories;

import org.example.eventy.common.models.Status;
import org.example.eventy.solutions.models.Category;

public class CategoryWithIDDTO {
    private Long id;
    private String name;
    private String description;
    private Status status;

    public CategoryWithIDDTO() {}

    public CategoryWithIDDTO(Long id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public CategoryWithIDDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.status = category.getStatus();
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
