package org.example.eventy.users.services;

import org.example.eventy.common.models.Status;
import org.example.eventy.users.models.Report;
import org.example.eventy.users.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    public Page<Report> getPendingReports(Pageable pageable) {
        return reportRepository.findAllByStatusOrderByIdDesc(pageable, Status.PENDING);
    }

    public Report getReport(Long reportId) {
        return reportRepository.findById(reportId).orElse(null);
    }

    public Report acceptReport(Report report) {
        report.setStatus(Status.ACCEPTED);
        return saveReport(report);
    }

    public Report declineReport(Report report) {
        report.setStatus(Status.DENIED);
        return saveReport(report);
    }

    public Report saveReport(Report report) {
        try {
            return reportRepository.save(report);
        }
        catch (Exception e) {
            return null;
        }
    }
}
