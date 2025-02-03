package org.example.eventy.reviews.dtos;

import org.example.eventy.common.models.Status;
import org.example.eventy.reviews.models.Review;

public class ReviewDTO {
    private Long id;
    private String comment;
    private Integer grade;
    private String senderEmail;
    private String recipientEmail;
    private String title; // event/product/service name
    private Status status;
    private Boolean isDeleted;

    public ReviewDTO() {

    }

    public ReviewDTO(Long id, String comment, Integer grade, String senderEmail, String recipientEmail, String title, Status status, Boolean isDeleted) {
        this.id = id;
        this.comment = comment;
        this.grade = grade;
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.title = title;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.comment = review.getComment();
        this.grade = review.getGrade();
        this.senderEmail = review.getGrader().getEmail();
        this.recipientEmail = review.getSolution() == null ? review.getEvent().getOrganiser().getEmail() : review.getSolution().getProvider().getEmail();
        this.title = review.getSolution() == null ? review.getEvent().getName() : review.getSolution().getName();
        this.status = review.getStatus();
        this.isDeleted = review.getDeleted();
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

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
