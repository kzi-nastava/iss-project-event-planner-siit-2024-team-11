package org.example.eventy.events.dtos;

import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.models.Event;

import java.time.LocalDateTime;

public class EventCardDTO {
    private Long eventId;
    private String name;
    private String description;
    private int maxNumberParticipants;
    private boolean isOpen;
    private String eventTypeName;
    private String locationName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long organiserId; // when we click on picture/name it shows organiser profile
    private String organiserName;
    private String organiserImage;

    public EventCardDTO() {
    }

    public EventCardDTO(Event event) {
        this.eventId = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.maxNumberParticipants = event.getMaxNumberParticipants();
        this.isOpen = event.getPrivacy().equals("PUBLIC");
        this.eventTypeName = event.getType().getName();
        this.locationName = event.getLocation().getName();
        this.startDate = event.getDate();
        this.organiserId = event.getOrganiser().getId();
        this.organiserName = event.getOrganiser().getFirstName() + " " + event.getOrganiser().getLastName();
        this.organiserImage = event.getOrganiser().getImageUrls() != null ? PictureService.getImage(event.getOrganiser().getImageUrls().get(0).getPath()) : "none";
    }

    public EventCardDTO(Long eventId, String name, String description, int maxNumberParticipants, boolean isOpen, String eventTypeName, String locationName, LocalDateTime startDate, LocalDateTime endDate, Long organiserId, String organiserName, String organiserImage) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.maxNumberParticipants = maxNumberParticipants;
        this.isOpen = isOpen;
        this.eventTypeName = eventTypeName;
        this.locationName = locationName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.organiserId = organiserId;
        this.organiserName = organiserName;
        this.organiserImage = organiserImage;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public Long getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(Long organiserId) {
        this.organiserId = organiserId;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    public String getOrganiserImage() {
        return organiserImage;
    }

    public void setOrganiserImage(String organiserImage) {
        this.organiserImage = organiserImage;
    }

    @Override
    public String toString() {
        return "EventCardDTO{" +
                "eventId=" + eventId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maxNumberParticipants=" + maxNumberParticipants +
                ", isOpen=" + isOpen +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", locationName='" + locationName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", organiserId=" + organiserId +
                ", organiserName='" + organiserName + '\'' +
                ", organiserImage='" + organiserImage + '\'' +
                '}';
    }
}
