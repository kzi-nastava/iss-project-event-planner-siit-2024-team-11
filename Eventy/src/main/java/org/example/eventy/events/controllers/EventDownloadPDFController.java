package org.example.eventy.events.controllers;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.example.eventy.events.models.Activity;
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

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RestController
@RequestMapping("/api/events/pdfs")
public class EventDownloadPDFController {
    @Autowired
    EventService eventService;

    @GetMapping(value = "/details/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> downloadEventDetailsPDF(@PathVariable Long eventId) {
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
            PdfFont font = PdfFontFactory.createFont("Helvetica-Bold");
            document.add(new Paragraph(event.getName())).setFont(font)
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(new Paragraph("Event Organizer: " + event.getOrganiser().getFirstName() + " " + event.getOrganiser().getLastName()))
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(new Paragraph(event.getDescription()))
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(new Paragraph("Location: " + event.getLocation().getAddress()))
                    .setFontSize(16);

            document.add(new Paragraph("Date: " + event.getDate()))
                    .setFontSize(16);

            document.add(new Paragraph("Agenda: " + event.getDate()))
                    .setFontSize(16);

            List unorderedList = new List()
                    .setSymbolIndent(14)
                    .setListSymbol("•")
                    .setFontSize(14);

            DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            for(Activity activity : event.getAgenda()) {
                unorderedList.add(new ListItem("Activity: " + activity.getName() +
                        "Description: " + activity.getDescription() +
                        "Start time: " + activity.getStartTime().format(shortFormatter) +
                        "End time: " + activity.getEndTime().format(shortFormatter)));
            }

            document.add(unorderedList);

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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
