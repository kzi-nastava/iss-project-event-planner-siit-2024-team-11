package org.example.eventy.users.services;

import org.example.eventy.users.dtos.ReportDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReportService {

    /*@Autowired
    private ReportRepository reportRepository;*/

    public ArrayList<ReportDTO> getReports(Pageable pageable) {
        ReportDTO report1 = new ReportDTO(
            1L,
            "Offensive language",
            "user123@example.com",
            "offender@example.com"
        );


        ReportDTO report2 = new ReportDTO(
            2L,
            "Inappropriate content",
            "johndoe22@example.com",
            "ns.services@example.com"
        );

        ArrayList<ReportDTO> reports = new ArrayList<>();
        reports.add(report1);
        reports.add(report2);

        return reports;
    }

    public ReportDTO getReport(Long reviewId) {
        ReportDTO report = new ReportDTO(
            reviewId,
            "Offensive language",
            "user123@example.com",
            "offender@example.com"
        );

        return report;
    }

    public ReportDTO createReport(ReportDTO report) {
        ReportDTO newReport = new ReportDTO();

        newReport.setId(report.getId());
        newReport.setReason(report.getReason());
        newReport.setSenderEmail(report.getSenderEmail());
        newReport.setReportedUserEmail(report.getReportedUserEmail());

        return saveReport(newReport);
    }

    private ReportDTO saveReport(ReportDTO report) {
        return report;
    }

    public Boolean deleteReport(Long reviewId) {
        //reportRepository.deleteById(reviewId);
        return true;
    }
}
