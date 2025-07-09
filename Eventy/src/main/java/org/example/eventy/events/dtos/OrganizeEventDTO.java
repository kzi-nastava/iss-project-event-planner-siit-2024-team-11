package org.example.eventy.events.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.example.eventy.events.validation.annotation.ValidOrganizedEvent;

import java.time.LocalDateTime;
import java.util.List;

@ValidOrganizedEvent // trigger the custom validation
public class OrganizeEventDTO {
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @Positive(message = "Number of participants must be positive")
    private int maxNumberParticipants;

    @NotNull(message = "Privacy type cannot be null")
    private boolean isPublic;

    @NotNull(message = "Event type cannot be null")
    private Long eventTypeId;

    @NotNull(message = "Location cannot be null")
    @Valid
    private CreateLocationDTO location;

    @NotNull(message = "Date cannot be null")
    @Future(message = "Date must be in the future")
    private LocalDateTime date;

    @NotEmpty(message = "Agenda cannot be empty")
    private List<@Valid CreateActivityDTO> agenda;

    private List<String> emails;

    @NotNull(message = "Organizer cannot be null")
    private Long organizerId;

    ///////////////////////////////

    public OrganizeEventDTO() {

    }

    public OrganizeEventDTO(String name, String description, int maxNumberParticipants, boolean isPublic, Long eventType, CreateLocationDTO location, LocalDateTime date, List<CreateActivityDTO> agenda, List<String> emails, Long organizerId) {
        this.name = name;
        this.description = description;
        this.maxNumberParticipants = maxNumberParticipants;
        this.isPublic = isPublic;
        this.eventTypeId = eventType;
        this.location = location;
        this.date = date;
        this.agenda = agenda;
        this.emails = emails;
        this.organizerId = organizerId;
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

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean aPublic) {
        isPublic = aPublic;
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
    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }
}
