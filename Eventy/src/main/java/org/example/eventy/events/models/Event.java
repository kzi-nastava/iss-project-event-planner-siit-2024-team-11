package org.example.eventy.events.models;

import org.example.eventy.users.models.EventOrganizer;

import java.time.LocalDateTime;
import java.util.List;

public class Event {
    private Long id;
    private String name;
    private String description;
    private int maxNumberParticipants;
    private PrivacyType privacy;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventType type;
    private Location location;
    private List<Activity> agenda;
    private EventOrganizer organiser;

    public Event() {

    }

    public Event(Long id, String name, String description, int maxNumberParticipants, PrivacyType privacy, LocalDateTime startDate, LocalDateTime endDate, EventType type, Location location, List<Activity> agenda, EventOrganizer organiser) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxNumberParticipants = maxNumberParticipants;
        this.privacy = privacy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.location = location;
        this.agenda = agenda;
        this.organiser = organiser;
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

    public int getMaxNumberParticipants() {
        return maxNumberParticipants;
    }

    public void setMaxNumberParticipants(int maxNumberParticipants) {
        this.maxNumberParticipants = maxNumberParticipants;
    }

    public PrivacyType getPrivacy() {
        return privacy;
    }

    public void setPrivacy(PrivacyType privacy) {
        this.privacy = privacy;
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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Activity> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<Activity> agenda) {
        this.agenda = agenda;
    }

    public EventOrganizer getOrganiser() {
        return organiser;
    }

    public void setOrganiser(EventOrganizer organiser) {
        this.organiser = organiser;
    }
}
