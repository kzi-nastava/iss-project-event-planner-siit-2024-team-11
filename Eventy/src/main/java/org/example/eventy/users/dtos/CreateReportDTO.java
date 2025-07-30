package org.example.eventy.users.dtos;

import org.example.eventy.users.models.Report;
import org.example.eventy.users.validation.annotation.ValidCreateReport;

@ValidCreateReport
public class CreateReportDTO {
    private String reason;
    private Long senderUserId;
    private Long reportedUserId;

    public CreateReportDTO() {}

    public CreateReportDTO(String reason, Long senderUserId, Long reportedUserId) {
        this.reason = reason;
        this.senderUserId = senderUserId;
        this.reportedUserId = reportedUserId;
    }

    public CreateReportDTO(Report report) {
        this.reason = report.getReason();
        this.senderUserId = report.getSender().getId();
        this.reportedUserId = report.getReportedUser().getId();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public Long getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(Long reportedUserId) {
        this.reportedUserId = reportedUserId;
    }
}
