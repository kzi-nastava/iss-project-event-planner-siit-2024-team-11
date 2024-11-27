package org.example.eventy.events.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class EventDTO {
    private String name;
    private String description;
    private int maxNumberParticipants;
    private boolean isOpen;
    private EventTypeDTO eventType;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<ActivityDTO> agenda;

    public EventDTO() {

    }

    public EventDTO(String name, String description, int maxNumberParticipants, boolean isOpen, EventTypeDTO eventType, String location, LocalDateTime startDate, LocalDateTime endDate, List<ActivityDTO> agenda) {
        this.name = name;
        this.description = description;
        this.maxNumberParticipants = maxNumberParticipants;
        this.isOpen = isOpen;
        this.eventType = eventType;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.agenda = agenda;
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

    public int getMaxNumberParticipants() {
        return maxNumberParticipants;
    }

    public void setMaxNumberParticipants(int maxNumberParticipants) {
        this.maxNumberParticipants = maxNumberParticipants;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public EventTypeDTO getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeDTO eventType) {
        this.eventType = eventType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<ActivityDTO> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<ActivityDTO> agenda) {
        this.agenda = agenda;
    }
}
