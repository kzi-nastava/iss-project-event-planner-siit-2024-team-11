package org.example.eventy.events.dtos;

import org.example.eventy.events.models.Activity;

import java.time.LocalDateTime;

public class ActivityDTO {
    private Long id;
    private String name;
    private String description;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ActivityDTO() {

    }

    public ActivityDTO(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.description = activity.getDescription();
        this.location = activity.getLocation();
        this.startTime = activity.getStartTime();
        this.endTime = activity.getEndTime();
    }

    public ActivityDTO(Long id, String name, String description, String location, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
