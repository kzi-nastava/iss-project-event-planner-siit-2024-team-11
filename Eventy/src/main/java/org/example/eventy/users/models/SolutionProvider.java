package org.example.eventy.users.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.example.eventy.solutions.models.Solution;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Provider")
public class SolutionProvider extends User {
    @Column()
    private String name;
    @Column()
    private String description;

    public SolutionProvider() {

    }

    public SolutionProvider(String name, String description) {
        this.name = name;
        this.description = description;
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
}
