package org.example.eventy.events.dtos;

import org.example.eventy.events.models.Event;
import org.example.eventy.users.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventDetailsDTO {
    private Long id;
    private String name;
    private String description;
    private EventTypeDTO eventType;
    private LocationDTO location;
    private LocalDateTime date;
    private List<ActivityDTO> agenda;
    private Long organizerId;
    private String organizerName;
    private boolean isFavorite;

    public EventDetailsDTO() {

    }

    public EventDetailsDTO(Event event, User loggedInUser) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.eventType = new EventTypeDTO(event.getType());
        this.location = new LocationDTO(event.getLocation());
        this.date = event.getDate();
        this.agenda = event.getAgenda().stream().map(ActivityDTO::new).collect(Collectors.toList());
        this.organizerId = event.getOrganiser().getId();
        this.organizerName = event.getOrganiser().getFirstName() + " " + event.getOrganiser().getLastName();
        this.isFavorite = loggedInUser != null && loggedInUser.getFavoriteEvents().contains(event);
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

    public EventTypeDTO getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeDTO eventType) {
        this.eventType = eventType;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<ActivityDTO> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<ActivityDTO> agenda) {
        this.agenda = agenda;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
