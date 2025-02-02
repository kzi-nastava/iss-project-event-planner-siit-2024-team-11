package org.example.eventy.interactions.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.example.eventy.users.models.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column()
    private Long redirectionId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grader_id", referencedColumnName = "id")
    @JsonManagedReference
    private User grader;

    @Column()
    private Integer grade;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    ///////////////////////////////////////////

    public Notification() {}

    public Notification(NotificationType type, Long redirectionId, String title, String message, User grader, Integer grade, LocalDateTime timestamp) {
        this.type = type;
        this.redirectionId = redirectionId;
        this.title = title;
        this.message = message;
        this.grader = grader;
        this.grade = grade;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Long getRedirectionId() {
        return redirectionId;
    }

    public void setRedirectionId(Long redirectionId) {
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

    public User getGrader() {
        return grader;
    }

    public void setGrader(User grader) {
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
                "id=" + id +
                ", type=" + type +
                ", redirectionId=" + redirectionId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", grader=" + grader +
                ", grade=" + grade +
                ", timestamp=" + timestamp +
                '}';
    }
}
