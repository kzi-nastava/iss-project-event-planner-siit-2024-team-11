package org.example.eventy.solutions.models;

import org.example.eventy.common.models.Status;
import org.example.eventy.users.models.User;

public class Review {
    private String comment;
    private int grade;
    private Status status;
    private User user;


    public Review(String comment, int grade, Status status, User user) {
        this.comment = comment;
        this.grade = grade;
        this.status = status;
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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
