package org.example.eventy.users.models;

public class Report {
    private Long id;
    private String reason;
    private User sender;
    private User reportedUser;

    public Report() {
    }

    public Report(Long id, String reason, User sender, User reportedUser) {
        this.id = id;
        this.reason = reason;
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
