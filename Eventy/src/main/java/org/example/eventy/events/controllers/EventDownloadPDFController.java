package org.example.eventy.events.controllers;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.example.eventy.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events/pdfs")
public class EventDownloadPDFController {
    @Autowired
    EventService eventService;

    @GetMapping(value = "/details/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> downloadEventDetailsPDF(@PathVariable Long eventId) {
        if(eventId == 5) {
            return new ResponseEntity<String>("Downloaded", HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/guest-list/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> downloadGuestListPDF(@PathVariable Long eventId) {
        Event event = eventService.getEvent(eventId);

        if(event == null) {
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            // Create PDF Writer
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);

            // Create PDF Document
            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            // Add content to the PDF
            document.add(new Paragraph("Guest List for: " + event.getName()));

            int iteratingNumber = 1;
            for(User user : eventService.getAttendingUsersByEvent(event.getId())) {
                document.add(new Paragraph(iteratingNumber++ + ". " + user.getEmail()));
            }

            // Close the document
            document.close();

            // Set headers for PDF download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=guest_list_event_" + eventId + ".pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            // Return the PDF as a byte array
            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/stats/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> downloadEventStatsPDF(@PathVariable Long eventId) {
        if(eventId == 5) {
            return new ResponseEntity<String>("Downloaded", HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
}
