package org.example.eventy.reviews.dtos;

import org.example.eventy.reviews.models.Review;
import org.example.eventy.reviews.validation.annotation.ValidCreateReview;

@ValidCreateReview
public class CreateReviewDTO {
    private Long graderId;
    private Long solutionId;
    private Long eventId;
    private Integer grade;
    private String comment;

    public CreateReviewDTO() {}

    public CreateReviewDTO(Long graderId, Long solutionId, Long eventId, Integer grade, String comment) {
        this.graderId = graderId;
        this.solutionId = solutionId;
        this.eventId = eventId;
        this.grade = grade;
        this.comment = comment;
    }

    public CreateReviewDTO(Review review) {
        this.graderId = review.getGrader().getId();
        this.solutionId = review.getSolution() != null ? review.getSolution().getId() : null;
        this.eventId = review.getEvent() != null ? review.getEvent().getId() : null;
        this.grade = review.getGrade();
        this.comment = review.getComment();
    }

    public Long getGraderId() {
        return graderId;
    }

    public void setGraderId(Long graderId) {
        this.graderId = graderId;
    }

    public Long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(Long solutionId) {
        this.solutionId = solutionId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CreateReviewDTO{" +
                "reviewerId=" + graderId +
                ", solutionId=" + solutionId +
                ", eventId=" + eventId +
                ", grade=" + grade +
                ", comment='" + comment + '\'' +
                '}';
    }
}
