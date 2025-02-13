package org.example.eventy.users.controllers;

import jakarta.validation.Valid;
import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.common.models.Status;
import org.example.eventy.users.dtos.CreateReportDTO;
import org.example.eventy.users.dtos.ReportDTO;
import org.example.eventy.users.models.Report;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.ReportService;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private UserService userService;

    // GET "/api/reports/pending"
    @GetMapping(value = "/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<ReportDTO>> getPendingReports(Pageable pageable) {
        Page<Report> pendingReports = reportService.getPendingReports(pageable);

        ArrayList<ReportDTO> pendingReportsDTO = new ArrayList<>();
        for (Report report : pendingReports) {
            pendingReportsDTO.add(new ReportDTO(report));
        }
        long count = pendingReports.getTotalElements();

        PagedResponse<ReportDTO> response = new PagedResponse<>(pendingReportsDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<ReportDTO>>(response, HttpStatus.OK);
    }

    // POST "/api/reports"
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateReportDTO> createReport(@Valid @RequestBody CreateReportDTO createReportDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // if there are validation errors, we return a 400 Bad Request response
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity(errorMessages, HttpStatus.BAD_REQUEST);
        }

        Report report = new Report();
        report.setReason(createReportDTO.getReason());
        report.setStatus(Status.PENDING);

        User sender = userService.get(createReportDTO.getSenderUserId());
        if (sender == null) {
            return new ResponseEntity<CreateReportDTO>(HttpStatus.BAD_REQUEST);
        }
        report.setSender(sender);

        User reportedUser = userService.get(createReportDTO.getReportedUserId());
        if (reportedUser == null) {
            return new ResponseEntity<CreateReportDTO>(HttpStatus.BAD_REQUEST);
        }
        report.setReportedUser(reportedUser);


        report = reportService.saveReport(report);
        if (report == null) {
            return new ResponseEntity<CreateReportDTO>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<CreateReportDTO>(new CreateReportDTO(report), HttpStatus.CREATED);
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
