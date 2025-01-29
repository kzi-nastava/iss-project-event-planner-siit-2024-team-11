package org.example.eventy.interactions.model;

import jakarta.persistence.*;
import org.example.eventy.users.models.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private NotificationType type;

    @Column()
    private Integer redirectionId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grader_id", referencedColumnName = "id")
    private User grader;

    @Column()
    private Integer grade;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    ///////////////////////////////////////////

    public Notification() {}

    public Notification(NotificationType type, Integer redirectionId, String title, String message, User grader, Integer grade, LocalDateTime timestamp) {
        this.type = type;
        this.redirectionId = redirectionId;
        this.title = title;
        this.message = message;
        this.grader = grader;
        this.grade = grade;
        this.timestamp = timestamp;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Integer getRedirectionId() {
        return redirectionId;
    }

    public void setRedirectionId(Integer redirectionId) {
        this.redirectionId = redirectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return grader;
    }

    public void setSender(User grader) {
        this.grader = grader;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "type=" + type +
                ", redirectionId=" + redirectionId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", grader=" + grader +
                ", grade=" + grade +
                ", timestamp=" + timestamp +
                '}';
    }
}
