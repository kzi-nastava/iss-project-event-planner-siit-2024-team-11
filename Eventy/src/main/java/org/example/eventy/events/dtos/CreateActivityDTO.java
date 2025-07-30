package org.example.eventy.events.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.eventy.events.models.Activity;

import java.time.LocalDateTime;

public class CreateActivityDTO {
    @NotEmpty(message = "Activity name cannot be empty!")
    private String name;
    @NotEmpty(message = "Activity description cannot be empty!")
    private String description;
    @NotEmpty(message = "Activity location cannot be empty!")
    private String location;
    @NotNull(message = "Activity start time cannot be empty!")
    private LocalDateTime startTime;
    @NotNull(message = "Activity end time cannot be empty!")
    private LocalDateTime endTime;

    public CreateActivityDTO() {

    }

    public CreateActivityDTO(Activity activity) {
        this.name = activity.getName();
        this.description = activity.getDescription();
        this.location = activity.getLocation();
        this.startTime = activity.getStartTime();
        this.endTime = activity.getEndTime();
    }

    public CreateActivityDTO(String name, String description, String location, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
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
