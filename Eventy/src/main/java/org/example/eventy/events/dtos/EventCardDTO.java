package org.example.eventy.events.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.models.PrivacyType;
import org.example.eventy.users.models.User;

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
    @JsonProperty("isFavorite")
    private boolean isFavorite;

    public EventCardDTO() {
    }

    public EventCardDTO(Event event, User loggedInUser) {
        this.eventId = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.maxNumberParticipants = event.getMaxNumberParticipants();
        this.isOpen = event.getPrivacy().equals(PrivacyType.PUBLIC);
        this.eventTypeName = event.getType().getName();
        this.locationName = event.getLocation().getName();
        this.startDate = event.getDate();
        this.organiserId = event.getOrganiser().getId();
        this.organiserName = event.getOrganiser().getFirstName() + " " + event.getOrganiser().getLastName();
        this.organiserImage = event.getOrganiser().getImageUrls() != null ? PictureService.getImage(event.getOrganiser().getImageUrls().get(0).getPath()) : "none";
        this.isFavorite = loggedInUser != null && loggedInUser.getFavoriteEvents().contains(event);
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

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean open) {
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

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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
                ", isFavorite=" + isFavorite + '\'' +
                '}';
    }
}
