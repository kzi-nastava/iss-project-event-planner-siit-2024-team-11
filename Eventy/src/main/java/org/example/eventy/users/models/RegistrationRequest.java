package org.example.eventy.users.models;

import org.example.eventy.common.models.Status;

import java.time.LocalDateTime;

public class RegistrationRequest {
    private Long id;
    private LocalDateTime date;
    private Status status;
    private User user;

    public RegistrationRequest() {

    }

    public RegistrationRequest(Long id, LocalDateTime date, Status status, User user) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
