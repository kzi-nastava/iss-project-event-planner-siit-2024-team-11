package org.example.eventy.events.controllers;

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
    @GetMapping(value = "/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> downloadEventDetailsPDF(@PathVariable("id") Long id) {
        if(id == 5) {
            return new ResponseEntity<String>("Downloaded", HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/guest-list/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> downloadGuestListPDF(@PathVariable("id") Long id) {
        if(id == 5) {
            return new ResponseEntity<String>("Downloaded", HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/stats/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> downloadEventStatsPDF(@PathVariable("id") Long id) {
        if(id == 5) {
            return new ResponseEntity<String>("Downloaded", HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
}
