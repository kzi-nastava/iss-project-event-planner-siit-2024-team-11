package org.example.eventy.solutions.dtos;

import org.example.eventy.solutions.models.Category;

public class CategoryDTO {
    private String name;
    private String description;

    public CategoryDTO() { super(); }

    public CategoryDTO(Category category) {
        this.name = category.getName();
        this.description = category.getDescription();
    }
}
