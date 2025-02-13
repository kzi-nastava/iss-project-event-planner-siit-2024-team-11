package org.example.eventy.users.models;

import jakarta.persistence.*;
import org.example.eventy.common.models.Status;

@Entity
@Table(name = "Reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // save as string values (e.g., "ACCEPTED")
    private Status status;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private User sender;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "reported_user_id", referencedColumnName = "id", nullable = false)
    private User reportedUser;

    ////////////////////////////////////

    public Report() {}

    public Report(Long id, String reason, Status status, User sender, User reportedUser) {
        this.id = id;
        this.reason = reason;
        this.status = status;
        this.sender = sender;
        this.reportedUser = reportedUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }
}
