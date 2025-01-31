package org.example.eventy.events.controllers;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Activity;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.models.Invitation;
import org.example.eventy.events.services.EventService;
import org.example.eventy.reviews.services.ReviewService;
import org.example.eventy.users.models.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/events/pdfs")
public class EventDownloadPDFController {
    @Autowired
    EventService eventService;

    @Autowired
    ReviewService reviewService;

    @GetMapping(value = "/details/{eventId}", produces = MediaType.APPLICATION_PDF_VALUE)
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
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + event.getName()  + "_details.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            // Return the PDF as a byte array
            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/guest-list/{eventId}", produces = MediaType.APPLICATION_PDF_VALUE)
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
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=guest_list_event_" + event.getName() + ".pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            // Return the PDF as a byte array
            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/stats/{eventId}", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasRole('Organizer') OR hasRole('Admin')")
    public ResponseEntity<byte[]> downloadEventStatsPDF(@PathVariable Long eventId) {
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

            document.add(new Paragraph(event.getName() + " - Stats"))
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(new Paragraph("Number of Visitors"))
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER);

            JFreeChart numberOfVisitorsChart = createDialChart(event.getInvitations().stream().filter(inv -> inv.getStatus() == Status.ACCEPTED).count(), event.getMaxNumberParticipants(), "Number of Visitors");
            document.add(createImageOfChart(numberOfVisitorsChart));

            document.add(new Paragraph("Grades"))
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER);

            java.util.List<Integer> grades = reviewService.getGradesForEvent(eventId);

            int[] numberOfGradesPerGrade = new int[5];
            double gradesSum = 0;

            for(int grade : grades) {
                numberOfGradesPerGrade[grade - 1]++;
                gradesSum += grade;
            }

            JFreeChart gradesChart = createBarChart(numberOfGradesPerGrade);
            document.add(createImageOfChart(gradesChart));

            document.add(new Paragraph("Average grade"))
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER);

            JFreeChart gradeAverageChart = createDialChart((double) (gradesSum / (double) grades.size()), 5, "Average Grade");
            document.add(createImageOfChart(gradeAverageChart));

            // Close the document
            document.close();

            // Set headers for PDF download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + event.getName()  + "_stats.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            // Return the PDF as a byte array
            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Image createImageOfChart(JFreeChart chart) throws IOException {
        // Convert chart to image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ChartUtils.writeChartAsPNG(baos, chart, 500, 400);
        ImageData imageData = ImageDataFactory.create(baos.toByteArray());
        return new Image(imageData);
    }

    private JFreeChart createBarChart(int[] grades) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(grades[0], "Grades", "1");
        dataset.addValue(grades[1], "Grades", "2");
        dataset.addValue(grades[2], "Grades", "3");
        dataset.addValue(grades[3], "Grades", "4");
        dataset.addValue(grades[4], "Grades", "5");

        return ChartFactory.createBarChart(
                "Grades",
                "Grade",
                "Number of Grades",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
    }

    private JFreeChart createDialChart(double scaleNumber, int max, String title) {
        DefaultValueDataset dataset = new DefaultValueDataset(scaleNumber);
        MeterPlot plot = new MeterPlot(dataset);
        plot.setRange(new Range(0, max));
        plot.setDialOutlinePaint(java.awt.Color.BLACK);
        return new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
    }
}
