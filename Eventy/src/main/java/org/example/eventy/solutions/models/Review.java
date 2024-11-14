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
}
