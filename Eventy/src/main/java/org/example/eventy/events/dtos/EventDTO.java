package org.example.eventy.events.dtos;

import org.example.eventy.events.models.Event;
import org.example.eventy.events.models.PrivacyType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private int maxNumberParticipants;
    private boolean isPublic;
    private EventTypeDTO eventType;
    private LocationDTO location;
    private LocalDateTime date;
    private List<ActivityDTO> agenda;
    private Long organizerId;

    public EventDTO() {

    }

    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.maxNumberParticipants = event.getMaxNumberParticipants();
        this.isPublic = event.getPrivacy() == PrivacyType.PUBLIC;
        this.eventType = new EventTypeDTO(event.getType());
        this.location = new LocationDTO(event.getLocation());
        this.date = event.getDate();
        this.agenda = event.getAgenda().stream().map(ActivityDTO::new).collect(Collectors.toList());
        this.organizerId = event.getOrganiser().getId();
    }

    public EventDTO(Long id, String name, String description, int maxNumberParticipants, boolean isPublic, EventTypeDTO eventType, LocationDTO location, LocalDateTime date, List<ActivityDTO> agenda, Long organizerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxNumberParticipants = maxNumberParticipants;
        this.isPublic = isPublic;
        this.eventType = eventType;
        this.location = location;
        this.date = date;
        this.agenda = agenda;
        this.organizerId = organizerId;
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
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
}
