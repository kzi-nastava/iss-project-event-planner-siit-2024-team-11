package org.example.eventy.users.models;

import org.example.eventy.solutions.models.Solution;

import java.util.ArrayList;
import java.util.List;

public class SolutionProvider extends User {
    private String name;
    private String description;
    private List<Solution> solutionCatalog;

    public SolutionProvider() {

    }

    public SolutionProvider(String name, String description) {
        this.name = name;
        this.description = description;
        this.solutionCatalog = new ArrayList<>();
    }

    public SolutionProvider(String name, String description, List<Solution> solutionCatalog) {
        this.name = name;
        this.description = description;
        this.solutionCatalog = solutionCatalog;
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

    public List<Solution> getSolutionCatalog() {
        return solutionCatalog;
    }

    public void setSolutionCatalog(List<Solution> solutionCatalog) {
        this.solutionCatalog = solutionCatalog;
    }
}
