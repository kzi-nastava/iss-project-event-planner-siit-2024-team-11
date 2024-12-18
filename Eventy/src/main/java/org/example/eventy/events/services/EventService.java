package org.example.eventy.events.services;

import org.example.eventy.common.models.PicturePath;
import org.example.eventy.events.models.*;
import org.example.eventy.events.repositories.EventRepository;
import org.example.eventy.users.models.EventOrganizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Page<Event> getEvents(String search, ArrayList<String> eventTypes, String location, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return eventRepository.findAll(search, eventTypes, location, startDate, endDate, pageable);
    }

    public Event getEvent(Long eventId) {
        ArrayList<Event> featuredEvents = generateEventExamples(1);
        Event event = featuredEvents.get(0);
        event.setId(eventId);

        return event;
    }

    public ArrayList<Event> getFeaturedEvents() {
        ArrayList<Event> featuredEvents = generateEventExamples(2);
        return featuredEvents;
    }

    public ArrayList<Event> generateEventExamples(int type) {
        EventType eventType = new EventType();
        eventType.setName("Event type name");
        Location loc = new Location();
        loc.setName("Location name");
        ArrayList<PicturePath> images = new ArrayList<PicturePath>();
        images.add(new PicturePath(1L, "Image 1")); images.add(new PicturePath(2L, "Image 2"));

        Event event1 = new Event();
        event1.setId(1L);
        event1.setName(type == 1 ? "Sophia's 25th Birthday Party" : "FEATURED - Sophia's 25th Birthday Party");
        event1.setDescription("A fun-filled evening to celebrate Sophia's birthday with friends and family.");
        event1.setMaxNumberParticipants(50);
        event1.setPrivacy(PrivacyType.PRIVATE);
        event1.setType(eventType);
        event1.setLocation(loc);
        event1.setDate(LocalDateTime.of(2024, 12, 15, 18, 0));
        event1.setOrganiser(new EventOrganizer());
        event1.getOrganiser().setFirstName("Tac Tac");
        event1.getOrganiser().setLastName("Jezickovic");
        event1.getOrganiser().setImageUrls(images);

        Event event2 = new Event();
        event2.setId(2L);
        event2.setName(type == 1 ? "2025 Annual Tech Conference" : "FEATURED - 2025 Annual Tech Conference");
        event2.setDescription("An annual conference to discuss the latest trends in technology and innovation.");
        event2.setMaxNumberParticipants(500);
        event2.setPrivacy(PrivacyType.PUBLIC);
        event2.setType(eventType);
        event2.setLocation(loc);
        event2.setDate(LocalDateTime.of(2025, 5, 20, 9, 0));
        event2.setOrganiser(new EventOrganizer());
        event2.getOrganiser().setFirstName("Tac Tac");
        event2.getOrganiser().setLastName("Jezickovic");
        event2.getOrganiser().setImageUrls(images);

        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);

        return events;
    }

    public Event save(Event event) {
        try {
            return eventRepository.save(event);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Event> getEventsByEventOrganizer(Long eventOrganizerId, String search, Pageable pageable) {
        return eventRepository.findByOrganizer(eventOrganizerId, search, pageable).getContent();
    }

    public long getEventsByEventOrganizerCount(Long eventOrganizerId) {
        return eventRepository.countByEventOrganizerId(eventOrganizerId);
    }

    public List<Event> getFavoriteEventsByUser(Long userId, String search, Pageable pageable) {
        return eventRepository.findUsersFavoriteEvents(userId, search, pageable).getContent();
    }

    public long getFavoriteEventsByUserCount(Long userId) {
        return eventRepository.countUsersFavoriteEvents(userId);
    }

    public List<Event> getOrganizedEventsByUserBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        return eventRepository.findOrganizedEventsByUserBetween(userId, startDate, endDate);
    }

    public List<Event> getAttendingEventsByUserBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        return eventRepository.findAttendingEventsByUserBetween(userId, startDate, endDate);
    }
}
