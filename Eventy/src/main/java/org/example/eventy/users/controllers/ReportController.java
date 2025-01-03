package org.example.eventy.users.controllers;

import org.example.eventy.users.dtos.ReportDTO;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.ReportService;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private UserService userService;

    // GET "/api/reports"
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReportDTO>> getReports(Pageable pageable) {
        ArrayList<ReportDTO> reports = reportService.getReports(pageable);
        return new ResponseEntity<Collection<ReportDTO>>(reports, HttpStatus.OK);
    }

    /*
    {
        "id": 5,
        "reason": "Offensive language",
        "senderEmail": "user123@example.com",
        "reportedUserEmail": "offender@example.com"
    }
    */
    // POST "/api/reports"
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> createReport(@RequestBody ReportDTO report) {
        if (report.getId() == 5) {
            ReportDTO newReport = reportService.createReport(report);
            return new ResponseEntity<ReportDTO>(newReport, HttpStatus.CREATED);
        }

        return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
    }

    // PUT "/api/reports/5/accept"
    @PutMapping(value = "/{reportId}/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> acceptReport(@PathVariable Long reportId) {
        if (reportId == 5) {
            ReportDTO report = reportService.getReport(reportId);

            User user = userService.suspendUser(report.getReportedUserEmail(), 3); // Assumes userService exists
            boolean isSuspended = user.getSuspensionDeadline() != null; // neka logika ovde
            if (!isSuspended) {
                return new ResponseEntity<>("Failed to suspend offender", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // delete the report after suspending
            Boolean isDeleted = reportService.deleteReport(reportId);
            return new ResponseEntity<>("Report accepted, offender suspended for 3 days", HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

    // DELETE "/api/reports/5"
    @DeleteMapping("/{reportId}")
    public ResponseEntity<ReportDTO> deleteReport(@PathVariable Long reportId) {
        if(reportId == 5) {
            ReportDTO report = reportService.getReport(reportId);
            reportService.deleteReport(reportId);
            report.setReason("DELETED in repo!!");

            return new ResponseEntity<ReportDTO>(report, HttpStatus.OK);
        }

        return new ResponseEntity<ReportDTO>(HttpStatus.NO_CONTENT);
    }
}
