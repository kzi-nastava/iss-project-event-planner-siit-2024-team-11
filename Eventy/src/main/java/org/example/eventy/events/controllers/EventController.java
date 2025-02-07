package org.example.eventy.events.controllers;

import jakarta.validation.Valid;
import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.common.models.Status;
import org.example.eventy.common.services.EmailService;
import org.example.eventy.events.dtos.*;
import org.example.eventy.events.models.*;
import org.example.eventy.events.services.*;
import org.example.eventy.interactions.dtos.NotificationDTO;
import org.example.eventy.interactions.model.Notification;
import org.example.eventy.interactions.model.NotificationType;
import org.example.eventy.interactions.services.NotificationService;
import org.example.eventy.reviews.services.ReviewService;
import org.example.eventy.users.models.EventOrganizer;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.example.eventy.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
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
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDetailsDTO> getEvent(@PathVariable Long eventId, @RequestHeader(value = "Authorization", required = false) String token) {
        Event event = eventService.getEvent(eventId);
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        if(event != null) {
            return new ResponseEntity<EventDetailsDTO>(new EventDetailsDTO(event, user), HttpStatus.OK);
        }

        return new ResponseEntity<EventDetailsDTO>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Organizer')")
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

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Organizer')")
    public ResponseEntity<EventDTO> editEvent(@Valid @RequestBody UpdateEventDTO updateEventDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // if there are validation errors, we return a 400 Bad Request response
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity(errorMessages, HttpStatus.BAD_REQUEST);
        }

        Event event = eventService.getEvent(updateEventDTO.getId());
        String oldEventName = event.getName();

        if(event == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        event.setName(updateEventDTO.getName());
        event.setDescription(updateEventDTO.getDescription());
        event.setMaxNumberParticipants(updateEventDTO.getMaxNumberParticipants());
        event.setDate(updateEventDTO.getDate());
        event.setType(eventTypeService.get(updateEventDTO.getEventTypeId()));
        event.setOrganiser((EventOrganizer) userService.get(updateEventDTO.getOrganizerId()));

        if(event.getLocation().getLatitude() != updateEventDTO.getLocation().getLatitude() || event.getLocation().getLongitude() != updateEventDTO.getLocation().getLongitude()) {
            Location location = new Location();
            location.setName(updateEventDTO.getLocation().getName());
            location.setAddress(updateEventDTO.getLocation().getAddress());
            location.setLatitude(updateEventDTO.getLocation().getLatitude());
            location.setLongitude(updateEventDTO.getLocation().getLongitude());

            //locationService.save(location); // is this even necessary with CascadeType.ALL
            event.setLocation(location);
        }

        List<Activity> agenda = new ArrayList<>();
        for(CreateActivityDTO activityDTO : updateEventDTO.getAgenda()) {
            Activity activity = new Activity();
            activity.setName(activityDTO.getName());
            activity.setDescription(activityDTO.getDescription());
            activity.setLocation(activityDTO.getLocation());
            activity.setStartTime(activityDTO.getStartTime());
            activity.setEndTime(activityDTO.getEndTime());
            //activityService.save(activity);
            agenda.add(activity);
        }

        event.getAgenda().clear();
        event.setAgenda(agenda);

        event = eventService.save(event);

        if(event != null) {
            EventDTO eventDTO = new EventDTO(event);
            List<Long> userIdsToNotify = userService.findUserIdsByAcceptedEventId(event.getId());

            for (Long userId : userIdsToNotify) {
                sendNotification(userId, event, oldEventName);
            }
            return new ResponseEntity<EventDTO>(eventDTO, HttpStatus.CREATED);
        }

        return new ResponseEntity<EventDTO>(HttpStatus.BAD_REQUEST);
    }

    private void sendNotification(Long userId, Event event, String oldEventName) {
        NotificationType type = NotificationType.EVENT_CHANGE;
        Long redirectionId = event.getId();
        String title = "\"" + oldEventName + "\"";
        Long ownerId = userId;
        User grader = null;
        Integer grade = null;
        String message = "NOTICE: The event has been changed! Tap to see more!";

        LocalDateTime timestamp = LocalDateTime.now();
        Notification notification = new Notification(type, redirectionId, title, message, grader, grade, timestamp);

        notificationService.saveNotification(ownerId, notification);
        sendNotificationToWeb(ownerId, notification);
        sendNotificationToMobile(ownerId, notification);
    }

    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Organizer') OR hasRole('Admin')")
    public ResponseEntity<PagedResponse<EventStatsDTO>> getEventsWithStats(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) ArrayList<String> eventTypes,
            @RequestParam(required = false, defaultValue = "") String location,
            @RequestParam(required = false, defaultValue = "9999") Integer maxParticipants,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable, @RequestHeader(value = "Authorization", required = false) String token) {
        // Pageable - page, size, sort
        // sort by: "type", "name", "maxNumberParticipants,asc", "maxNumberParticipants,desc", "location", "date,asc", "date,desc"

        // Set default values for startDate and endDate
        if (startDate == null) {
            startDate = LocalDateTime.of(1970, 1, 1, 0, 0); // Very small date
        }
        if (endDate == null) {
            endDate = LocalDateTime.of(2099, 12, 31, 23, 59); // Very large date
        }

        User user = null;
        if(token != null) {
            try {
                token = token.substring(7);
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        if (user == null) {
            return new ResponseEntity<PagedResponse<EventStatsDTO>>(HttpStatus.FORBIDDEN);
        }

        Page<Event> events;

        if(user.getRole().getName().equals("ROLE_Organizer")) {
            events = eventService.getPublicEventsForUser(search, eventTypes, maxParticipants, location, startDate, endDate, pageable, user);
        }
        else {
            events = eventService.getPublicEvents(search, eventTypes, maxParticipants, location, startDate, endDate, pageable);
        }

        User finalUser = user;
        List<EventStatsDTO> eventStatsDTOs = new ArrayList<>();
        for (Event event : events) {
            int visitors = (int) event.getInvitations().stream().filter(inv -> inv.getStatus() == Status.ACCEPTED).count();
            java.util.List<Integer> grades = reviewService.getGradesForEvent(event.getId());

            int[] gradeDistribution = new int[5];
            double gradesSum = 0;

            for(int grade : grades) {
                gradeDistribution[grade - 1]++;
                gradesSum += grade;
            }

            eventStatsDTOs.add(new EventStatsDTO(new EventCardDTO(event, finalUser), visitors, !grades.isEmpty() ? (double) (gradesSum / (double) grades.size()) : 0, gradeDistribution));
        }
        long count = events.getTotalElements();

        PagedResponse<EventStatsDTO> eventStats = new PagedResponse<EventStatsDTO>(eventStatsDTOs, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<EventStatsDTO>>(eventStats, HttpStatus.OK);
    }

    @PutMapping(value = "/favorite/{eventId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> toggleFavoriteEvent(@PathVariable Long eventId, @RequestHeader(value = "Authorization", required = false) String token) {
        Event event = eventService.getEvent(eventId);

        if(event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (token == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user;
        try {
            token = token.substring(7);
            user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));

            if(user == null) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.toggleFavoriteEvent(user, event);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/favorite/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<EventCardDTO>> getFavoriteEvents(@PathVariable Long userId, @RequestParam(required = false) String search,
                                                                  Pageable pageable, @RequestHeader(value = "Authorization", required = false) String token) {
        List<Event> favoriteEvents = eventService.getFavoriteEventsByUser(userId, search, pageable);

        User user = null;
        if(token != null) {
            try {
                token = token.substring(7);
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        User finalUser = user;
        List<EventCardDTO> eventCards = favoriteEvents.stream()
                .map(event -> new EventCardDTO(event, finalUser))
                .collect(Collectors.toList());
        long count = eventService.getFavoriteEventsByUserCount(userId);
        PagedResponse<EventCardDTO> response = new PagedResponse<>(eventCards, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<EventCardDTO>>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/organized/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagedResponse<EventCardDTO>> getEventsOrganizedByUser(@PathVariable Long userId, @RequestParam(required = false) String search,
                                                                                Pageable pageable, @RequestHeader(value = "Authorization", required = false) String token) {
        List<Event> organizersEvents = eventService.getEventsByEventOrganizer(userId, search, pageable);

        User user = null;
        if(token != null) {
            try {
                token = token.substring(7);
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        User finalUser = user;
        List<EventCardDTO> eventCards = organizersEvents.stream()
                .map(event -> new EventCardDTO(event, finalUser))
                .collect(Collectors.toList());
        long count = eventService.getEventsByEventOrganizerCount(userId);
        PagedResponse<EventCardDTO> response = new PagedResponse<>(eventCards, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<EventCardDTO>>(response, HttpStatus.OK);
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
            Pageable pageable, @RequestHeader(value = "Authorization", required = false) String token) {
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

        User user = null;
        if(token != null) {
            try {
                token = token.substring(7);
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        User finalUser = user;
        List<EventCardDTO> eventsDTO = new ArrayList<>();
        for (Event event : events) {
            eventsDTO.add(new EventCardDTO(event, finalUser));
        }
        long count = events.getTotalElements();

        PagedResponse<EventCardDTO> response = new PagedResponse<>(eventsDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<EventCardDTO>>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<EventCardDTO>> getEventsByUserId(
            @PathVariable Long userId,
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
        Page<Event> events = eventService.getEvents(userId, search, eventTypes, maxParticipants, location, startDate, endDate, pageable);

        User loggedInUser = userService.get(userId);
        List<EventCardDTO> eventsDTO = new ArrayList<>();
        for (Event event : events) {
            eventsDTO.add(new EventCardDTO(event, loggedInUser));
        }
        long count = events.getTotalElements();

        PagedResponse<EventCardDTO> response = new PagedResponse<>(eventsDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<EventCardDTO>>(response, HttpStatus.OK);
    }

    @GetMapping("/homepage-routing/")
    public ResponseEntity<Void> homepageRouting(@RequestHeader("User-Agent") String userAgent) {
        // if the device is mobile
        if (userAgent.toLowerCase().contains("android")) {
            String deepLink = "eventy://homepage";
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(deepLink));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

        String webLink = "http://localhost:4200/";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(webLink));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // GET "/api/events/cards/5"
    @GetMapping(value = "/cards/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventCardDTO> getEventCard(@PathVariable Long eventId, @RequestHeader(value = "Authorization", required = false) String token) {
        Event event = eventService.getEvent(eventId);

        if (event != null) {
            User user = null;
            if(token != null) {
                try {
                    token = token.substring(7);
                    user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
                }
                catch (Exception ignored) {
                }
            }

            User finalUser = user;
            EventCardDTO eventCardDTO = new EventCardDTO(event, finalUser);
            return new ResponseEntity<EventCardDTO>(eventCardDTO, HttpStatus.OK);
        }

        return new ResponseEntity<EventCardDTO>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/details-routing/{eventId}")
    public ResponseEntity<Void> eventDetailsRouting(@PathVariable String eventId, @RequestHeader("User-Agent") String userAgent) {
        // if the device is mobile
        if (userAgent.toLowerCase().contains("android")) {
            String deepLink = "eventy://event-details?id=" + eventId;
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(deepLink));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

        String webLink = "http://localhost:4200/events/" + eventId;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(webLink));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /* this returns EventCardDTOs, because there is NO CASE where:
      1) we need ALL FEATURED events
      2) they are NOT in card shapes (they always will be if we are getting all featured events) */
    // GET "/api/events/featured"
    @GetMapping(value = "/featured", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventCardDTO>> getFeaturedEvents(@RequestHeader(value = "Authorization", required = false) String token) {
        ArrayList<Event> featuredEvents = eventService.getFeaturedEvents();

        User user = null;
        if(token != null) {
            try {
                token = token.substring(7);
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        User finalUser = user;
        ArrayList<EventCardDTO> featuredEventsDTO = new ArrayList<>();
        for (Event event : featuredEvents) {
            featuredEventsDTO.add(new EventCardDTO(event, finalUser));
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

    @GetMapping(value = "/unreviewed/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UnreviewedEventDTO>> getUnreviewedAcceptedEventsByUserId(@PathVariable Long userId) {
        ArrayList<Event> unreviewedAcceptedEvents = eventService.getUnreviewedAcceptedEventsByUserId(userId);
        ArrayList<UnreviewedEventDTO> unreviewedEventDTO = new ArrayList<UnreviewedEventDTO>();

        for(Event event : unreviewedAcceptedEvents) {
            unreviewedEventDTO.add(new UnreviewedEventDTO(event));
        }

        return new ResponseEntity<Collection<UnreviewedEventDTO>>(unreviewedEventDTO, HttpStatus.OK);
    }

    private void sendNotificationToWeb(Long userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/web/" + userId, new NotificationDTO(notification));
    }

    private void sendNotificationToMobile(Long userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/mobile/" + userId, new NotificationDTO(notification));
    }
}

