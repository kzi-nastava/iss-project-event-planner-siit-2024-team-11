package org.example.eventy.events.controllers;

import jakarta.validation.Valid;
import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.common.services.EmailService;
import org.example.eventy.events.dtos.*;
import org.example.eventy.events.models.*;
import org.example.eventy.events.services.*;
import org.example.eventy.users.models.EventOrganizer;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;

    @GetMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long eventId) {
        if(eventId == 5) {
            return new ResponseEntity<EventDTO>(new EventDTO(), HttpStatus.OK);
        }

        return new ResponseEntity<EventDTO>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('Organizer')")
    public ResponseEntity<EventDTO> organizeEvent(@Valid @RequestBody OrganizeEventDTO organizeEventDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // if there are validation errors, we return a 400 Bad Request response
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity(errorMessages, HttpStatus.BAD_REQUEST);
        }

        Event event = new Event();
        event.setName(organizeEventDTO.getName());
        event.setDescription(organizeEventDTO.getDescription());
        event.setMaxNumberParticipants(organizeEventDTO.getMaxNumberParticipants());
        event.setPrivacy(organizeEventDTO.getIsPublic() ? PrivacyType.PUBLIC : PrivacyType.PRIVATE);
        event.setDate(organizeEventDTO.getDate());
        event.setType(eventTypeService.get(organizeEventDTO.getEventTypeId()));
        event.setOrganiser((EventOrganizer) userService.get(organizeEventDTO.getOrganizerId()));

        Location location = new Location();
        location.setName(organizeEventDTO.getLocation().getName());
        location.setAddress(organizeEventDTO.getLocation().getAddress());
        location.setLatitude(organizeEventDTO.getLocation().getLatitude());
        location.setLongitude(organizeEventDTO.getLocation().getLongitude());
        //locationService.save(location); // is this even necessary with CascadeType.ALL
        event.setLocation(location);

        List<Activity> agenda = new ArrayList<>();
        for(CreateActivityDTO activityDTO : organizeEventDTO.getAgenda()) {
            Activity activity = new Activity();
            activity.setName(activityDTO.getName());
            activity.setDescription(activityDTO.getDescription());
            activity.setLocation(activityDTO.getLocation());
            activity.setStartTime(activityDTO.getStartTime());
            activity.setEndTime(activityDTO.getEndTime());
            //activityService.save(activity);
            agenda.add(activity);
        }
        event.setAgenda(agenda);

        event = eventService.save(event);

        if(event != null) {
            if (event.getPrivacy() == PrivacyType.PRIVATE) {
                // SEND EMAIL INVITATIONS HERE, example:
                emailService.sendInvitations(event, organizeEventDTO.getEmails());
            }
            EventDTO eventDTO = new EventDTO(event);
            // needs to be this ---> // EventDTO eventDTO = new EventDTO(event);
            // but there are right now errors while converting to DTO (getOrganizer().getId()... <=> null.getId()...)
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
    public ResponseEntity<PagedResponse<EventCardDTO>> getFavoriteEvents(@PathVariable Long userId, @RequestParam(required = false) String search,
                                                                  Pageable pageable) {
        List<Event> favoriteEvents = eventService.getFavoriteEventsByUser(userId, search, pageable);

        List<EventCardDTO> eventCards = favoriteEvents.stream().map(EventCardDTO::new).collect(Collectors.toList());
        long count = eventService.getFavoriteEventsByUserCount(userId);
        PagedResponse<EventCardDTO> response = new PagedResponse<>(eventCards, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<EventCardDTO>>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/organized/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagedResponse<EventCardDTO>> getEventsOrganizedByUser(@PathVariable Long userId, @RequestParam(required = false) String search,
                                                                                Pageable pageable) {
        List<Event> organizersEvents = eventService.getEventsByEventOrganizer(userId, search, pageable);

        List<EventCardDTO> eventCards = organizersEvents.stream().map(EventCardDTO::new).collect(Collectors.toList());
        long count = eventService.getEventsByEventOrganizerCount(userId);
        PagedResponse<EventCardDTO> response = new PagedResponse<>(eventCards, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<EventCardDTO>>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/accepted/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
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
    // GET /api/events?search=Jane%20%26%20Mark%20Wedding&eventTypes=Wedding,Party&location=BeachResort&startDate=2024-05-01T00:00:00&endDate=2024-05-01T00:00:00&page=0&size=5&sort=date,asc
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<EventCardDTO>> getEvents(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) ArrayList<String> eventTypes,
            @RequestParam(required = false, defaultValue = "") String location,
            @RequestParam(required = false, defaultValue = "9999") Integer maxParticipants,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        // Pageable - page, size, sort
        // sort by: "type", "name", "maxNumberParticipants,asc", "maxNumberParticipants,desc", "location", "date,asc", "date,desc"

        // Set default values for startDate and endDate
        if (startDate == null) {
            startDate = LocalDateTime.of(1970, 1, 1, 0, 0); // Very small date
        }
        if (endDate == null) {
            endDate = LocalDateTime.of(2099, 12, 31, 23, 59); // Very large date
        }
        Page<Event> events = eventService.getEvents(search, eventTypes, maxParticipants, location, startDate, endDate, pageable);

        List<EventCardDTO> eventsDTO = new ArrayList<>();
        for (Event event : events) {
            eventsDTO.add(new EventCardDTO(event));
        }
        long count = events.getTotalElements();

        PagedResponse<EventCardDTO> response = new PagedResponse<>(eventsDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<EventCardDTO>>(response, HttpStatus.OK);
    }

    // GET "/api/events/cards/5"
    @GetMapping(value = "/cards/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventCardDTO> getEventCard(@PathVariable Long eventId) {
        Event event = eventService.getEvent(eventId);

        if (event != null) {
            EventCardDTO eventCardDTO = new EventCardDTO(event);
            return new ResponseEntity<EventCardDTO>(eventCardDTO, HttpStatus.OK);
        }

        return new ResponseEntity<EventCardDTO>(HttpStatus.NOT_FOUND);
    }

    /* this returns EventCardDTOs, because there is NO CASE where:
      1) we need ALL FEATURED events
      2) they are NOT in card shapes (they always will be if we are getting all featured events) */
    // GET "/api/events/featured"
    @GetMapping(value = "/featured", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventCardDTO>> getFeaturedEvents() {
        ArrayList<Event> featuredEvents = eventService.getFeaturedEvents();

        ArrayList<EventCardDTO> featuredEventsDTO = new ArrayList<>();
        for (Event event : featuredEvents) {
            featuredEventsDTO.add(new EventCardDTO(event));
        }

        return new ResponseEntity<Collection<EventCardDTO>>(featuredEventsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/event-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<String>> getAllUniqueEventTypesForEvents() {
        ArrayList<String> eventTypeNames = eventService.getAllUniqueEventTypesForEvents();
        return new ResponseEntity<Collection<String>>(eventTypeNames, HttpStatus.OK);
    }

    @GetMapping(value = "/locations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<String>> getAllUniqueLocationsForEvents() {
        ArrayList<String> locationNames = eventService.getAllUniqueLocationsForEvents();
        return new ResponseEntity<Collection<String>>(locationNames, HttpStatus.OK);
    }
}

