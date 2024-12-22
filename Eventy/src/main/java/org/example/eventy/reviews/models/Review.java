package org.example.eventy.reviews.models;

import jakarta.persistence.*;
import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Event;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.users.models.User;

@Entity
@Table(name = "Reviews",
       uniqueConstraints = {
          @UniqueConstraint(columnNames = {"user_id", "event_id"}),
          @UniqueConstraint(columnNames = {"user_id", "solution_id"})
       })
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User sender;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = true)
    private Event event;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "solution_id", referencedColumnName = "id", nullable = true)
    private Solution solution;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = true)
    private Integer grade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // save as string values (e.g., "ACCEPTED")
    private Status status;

    @Column(nullable = false)
    private Boolean isDeleted;

    ////////////////////////////////////

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
