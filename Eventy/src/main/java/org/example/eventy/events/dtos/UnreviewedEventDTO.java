package org.example.eventy.events.dtos;

import org.example.eventy.events.models.Event;

public class UnreviewedEventDTO {
    private Long id;
    private String name;

    public UnreviewedEventDTO() {}

    public UnreviewedEventDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UnreviewedEventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
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
}
