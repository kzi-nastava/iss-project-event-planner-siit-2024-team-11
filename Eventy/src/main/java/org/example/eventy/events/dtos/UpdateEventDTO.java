package org.example.eventy.events.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.validation.annotation.ValidUpdateEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ValidUpdateEvent // trigger the custom validation
public class UpdateEventDTO {
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Description cannot be null")
    private String description;

    @NotNull(message = "Number of participants cannot be null")
    private int maxNumberParticipants;

    @NotNull(message = "Event type cannot be null")
    private Long eventTypeId;

    @NotNull(message = "Location cannot be null")
    private CreateLocationDTO location;

    @NotNull(message = "Date cannot be null")
    @Future(message = "Date must be in the future")
    private LocalDateTime date;

    @NotNull(message = "Agenda cannot be null")
    private List<CreateActivityDTO> agenda;

    ///////////////////////////////

    public UpdateEventDTO() {

    }

    public UpdateEventDTO(Long id, String name, String description, int maxNumberParticipants, Long eventType, CreateLocationDTO location, LocalDateTime date, List<CreateActivityDTO> agenda) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxNumberParticipants = maxNumberParticipants;
        this.eventTypeId = eventType;
        this.location = location;
        this.date = date;
        this.agenda = agenda;
    }

    public UpdateEventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.maxNumberParticipants = event.getMaxNumberParticipants();
        this.eventTypeId = event.getType().getId();
        this.location = new CreateLocationDTO(event.getLocation().getName(), event.getLocation().getAddress(),
                event.getLocation().getLatitude(), event.getLocation().getLongitude());
        this.date = event.getDate();
        this.agenda = event.getAgenda().stream().map(CreateActivityDTO::new).collect(Collectors.toList());
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

    public Long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public CreateLocationDTO getLocation() {
        return location;
    }

    public void setLocation(CreateLocationDTO location) {
        this.location = location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<CreateActivityDTO> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<CreateActivityDTO> agenda) {
        this.agenda = agenda;
    }
}
