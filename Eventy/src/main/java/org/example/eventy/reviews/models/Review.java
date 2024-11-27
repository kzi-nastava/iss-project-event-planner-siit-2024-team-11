package org.example.eventy.reviews.models;

import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Event;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.users.models.User;

public class Review {
    private Long id;
    private User sender;
    private Event event;
    private Solution solution;
    private String comment;
    private Integer grade;
    private Status status;
    private Boolean isDeleted;

    public Review() {
    }

    public Review(Long id, User sender, Event event, Solution solution, String comment, Integer grade, Status status, Boolean isDeleted) {
        this.id = id;
        this.sender = sender;
        this.event = event;
        this.solution = solution;
        this.comment = comment;
        this.grade = grade;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
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
