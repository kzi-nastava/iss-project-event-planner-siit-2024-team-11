package org.example.eventy.events.dtos;

public class EventTypeDTO {
    private String name;
    private String description;
    // private List<SolutionDTO> recommendedSolutions;

    public EventTypeDTO() {

    }

    public EventTypeDTO(String name, String description) {
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