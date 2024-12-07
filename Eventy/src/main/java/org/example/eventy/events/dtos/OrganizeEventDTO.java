package org.example.eventy.events.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class OrganizeEventDTO {
    private String name;
    private String description;
    private int maxNumberParticipants;
    private boolean isPublic;
    private Long eventTypeId;
    private CreateLocationDTO location;
    private LocalDateTime date;
    private List<CreateActivityDTO> agenda;
    private List<String> emails;
    private Long organizerId;

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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
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
