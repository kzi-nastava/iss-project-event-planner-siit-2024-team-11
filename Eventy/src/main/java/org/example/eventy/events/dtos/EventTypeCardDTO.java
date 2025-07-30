package org.example.eventy.events.dtos;

import org.example.eventy.events.models.EventType;

public class EventTypeCardDTO {
    private Long id;
    private String name;

    public EventTypeCardDTO() {

    }

    public EventTypeCardDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public EventTypeCardDTO(EventType eventType) {
        this.id = eventType.getId();
        this.name = eventType.getName();
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
