package org.example.eventy.events.controllers;

import org.example.eventy.events.dtos.EventDTO;
import org.example.eventy.events.dtos.EventStatsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        if(eventDTO.getName().equals("event")) {
            return new ResponseEntity<EventDTO>(eventDTO, HttpStatus.CREATED);
        }

        return new ResponseEntity<EventDTO>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/{eventId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long eventId) {
        if(eventId == 5) {
            return new ResponseEntity<EventDTO>(new EventDTO(), HttpStatus.OK);
        }

        return new ResponseEntity<EventDTO>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventStatsDTO>> getEventsWithStats() {
        List<EventStatsDTO> eventStatsDTOs = new ArrayList<EventStatsDTO>();
        return new ResponseEntity<Collection<EventStatsDTO>>(eventStatsDTOs, HttpStatus.OK);
    }

    @PutMapping(value = "/favorite", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDTO> favoriteEvent(@RequestBody EventDTO eventDTO) {
        if(eventDTO.getName().equals("event")) {
            // here we call service function that will make this a favorite of the currently logged-in user
            return new ResponseEntity<EventDTO>(HttpStatus.OK);
        }

        return new ResponseEntity<EventDTO>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/favorite/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventDTO>> getFavoriteEvents(@PathVariable Long userId) {
        if(userId == 5) {
            List<EventDTO> events = new ArrayList<EventDTO>();
            return new ResponseEntity<Collection<EventDTO>>(events, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<EventDTO>>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/organized/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventDTO>> getEventsOrganizedByUser(@PathVariable Long userId) {
        if(userId == 5) {
            // check if this is a valid id of a organizer first
            List<EventDTO> events = new ArrayList<EventDTO>();
            return new ResponseEntity<Collection<EventDTO>>(events, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<EventDTO>>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/accepted/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventDTO>> getAcceptedEventsByUser(@PathVariable Long userId) {
        if(userId == 5) {
            List<EventDTO> events = new ArrayList<EventDTO>();
            return new ResponseEntity<Collection<EventDTO>>(events, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<EventDTO>>(HttpStatus.NOT_FOUND);
    }
}