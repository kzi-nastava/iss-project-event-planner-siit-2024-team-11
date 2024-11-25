package org.example.eventy.reviews.dtos;

import org.example.eventy.common.models.Status;
import org.example.eventy.reviews.models.Review;

public class CreatedReviewDTO {
    private Long id;
    private String comment;
    private Integer grade;
    private Long reviewerId;
    private String solutionName;
    private String eventName;
    private Status status;

    public CreatedReviewDTO() {

    }

    public CreatedReviewDTO(Review review) {
        this.id = review.getId();
        this.comment = review.getComment();
        this.grade = review.getGrade();
        if (review.getSolution() != null) {
            this.solutionName = review.getSolution().getName();
        }
        this.eventName = review.getEvent().getName();
        this.status = review.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
