package org.example.eventy.reviews.dtos;

public class UpdateReviewDTO {
    private String comment;
    private Integer grade;

    public UpdateReviewDTO() {
    }

    public UpdateReviewDTO(String comment, Integer grade) {
        this.comment = comment;
        this.grade = grade;
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
}
