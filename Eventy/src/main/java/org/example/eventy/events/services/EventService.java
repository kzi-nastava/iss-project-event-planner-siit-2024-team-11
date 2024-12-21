package org.example.eventy.events.services;

import org.example.eventy.events.models.*;
import org.example.eventy.events.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<Event> getEvents(String search, ArrayList<String> eventTypes, Integer maxParticipants, String location, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return eventRepository.findAll(search, eventTypes, maxParticipants, location, startDate, endDate, pageable);
    }

    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    public ArrayList<Event> getFeaturedEvents() {
        Pageable pageable = PageRequest.of(0, 5);
        return eventRepository.findFeaturedEvents(pageable);
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
