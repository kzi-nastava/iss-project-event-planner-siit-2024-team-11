package org.example.eventy.reviews.models;

import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Event;
import org.example.eventy.solutions.models.Service;

public class Review {
    private Event event;
    private Service service;
    private String comment;
    private Integer grade;
    private Status status;
    private Boolean isDeleted;

    public Review() {
    }

    public Review(Event event, Service service, String comment, Integer grade, Status status, Boolean isDeleted) {
        this.event = event;
        this.service = service;
        this.comment = comment;
        this.grade = grade;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
