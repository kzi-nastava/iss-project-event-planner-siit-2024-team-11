package org.example.eventy.reviews.dtos;

import org.example.eventy.common.models.Status;

public class UpdateReviewDTO {
    private String comment;
    private Integer grade;
    private Status status;

    public UpdateReviewDTO() {
    }

    public UpdateReviewDTO(String comment, Integer grade, Status status) {
        this.comment = comment;
        this.grade = grade;
        this.status = status;
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
}
