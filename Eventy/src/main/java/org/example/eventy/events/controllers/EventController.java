package org.example.eventy.events.controllers;

import org.example.eventy.events.dtos.EventCardDTO;
import org.example.eventy.events.dtos.EventDTO;
import org.example.eventy.events.dtos.EventStatsDTO;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping(value = "/{eventId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long eventId) {
        if(eventId == 5) {
            return new ResponseEntity<EventDTO>(new EventDTO(), HttpStatus.OK);
        }

        return new ResponseEntity<EventDTO>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        if(eventDTO.getName().equals("event")) {
            return new ResponseEntity<EventDTO>(eventDTO, HttpStatus.CREATED);
        }

        return new ResponseEntity<EventDTO>(HttpStatus.BAD_REQUEST);
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

    /* this returns EventCardDTOs, because there is NO CASE where:
       1) we need ALL events
       2) they are NOT in card shapes (they always will be if we are getting all events) */
    /* NOTE: search: Jane & Mark Wedding (%20 == " ", %26 == "&") */
    // GET /api/events?search=Jane%20%26%20Mark%20Wedding&eventTypes=Wedding,Party&location=BeachResort&startDate=2024-05-01&endDate=2024-05-31&page=0&size=5&sort=date,asc
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventCardDTO>> getEvents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ArrayList<String> eventTypes,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        // Pageable - page, size, sort
        // sort by: "eventType", "name", "maxParticipants,asc", "maxParticipants,desc", "location", "date,asc", "date,desc"
        ArrayList<Event> eventModels = eventService.getEvents(search, eventTypes, location, startDate, endDate, pageable);

        ArrayList<EventCardDTO> events = new ArrayList<>();
        for (Event event : eventModels) {
            events.add(new EventCardDTO(event));
        }

        return new ResponseEntity<Collection<EventCardDTO>>(events, HttpStatus.OK);
    }

    // GET "/api/events/cards/5"
    @GetMapping(value = "/cards/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventCardDTO> getEventCard(@PathVariable Long eventId) {
        if (eventId == 5) {
            Event eventModel = eventService.getEvent(eventId);
            EventCardDTO eventCard = new EventCardDTO(eventModel);

            return new ResponseEntity<EventCardDTO>(eventCard, HttpStatus.OK);
        }

        return new ResponseEntity<EventCardDTO>(HttpStatus.NOT_FOUND);
    }

    /* this returns EventCardDTOs, because there is NO CASE where:
      1) we need ALL FEATURED events
      2) they are NOT in card shapes (they always will be if we are getting all featured events) */
    // GET "/api/events/featured"
    @GetMapping(value = "/featured", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventCardDTO>> getFeaturedEvents() {
        ArrayList<Event> featuredEventModels = eventService.getFeaturedEvents();

        ArrayList<EventCardDTO> featuredEvents = new ArrayList<>();
        for (Event event : featuredEventModels) {
            featuredEvents.add(new EventCardDTO(event));
        }

        return new ResponseEntity<Collection<EventCardDTO>>(featuredEvents, HttpStatus.OK);
    }
}

