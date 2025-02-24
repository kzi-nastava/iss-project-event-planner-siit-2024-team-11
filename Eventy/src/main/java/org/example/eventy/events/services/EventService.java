package org.example.eventy.events.services;

import org.example.eventy.events.models.*;
import org.example.eventy.events.repositories.EventRepository;
import org.example.eventy.users.models.User;
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

    public Page<Event> getEvents(String search, ArrayList<String> eventTypes, Integer maxParticipants, String location, LocalDateTime startDate, LocalDateTime endDate, User user, Pageable pageable) {
        StringBuilder blockedUsersConcatenated = new StringBuilder();

        if (user != null) {
            List<User> blockedUsers = user.getBlocked();
            if (blockedUsers != null && !blockedUsers.isEmpty()) {
                for (User blockedUser : blockedUsers) {
                    blockedUsersConcatenated.append(blockedUser.getId()).append(",");
                }
                blockedUsersConcatenated.deleteCharAt(blockedUsersConcatenated.length() - 1);
            }

            if (user.getRole().getName().equals("ROLE_AuthenticatedUser")) {
                List<Long> organizerIdsWhoBlockedUser = eventRepository.findAllOrganizerIdsWhoBlockedUserId(user.getId());
                if (organizerIdsWhoBlockedUser != null && !organizerIdsWhoBlockedUser.isEmpty()) {
                    for (Long id : organizerIdsWhoBlockedUser) {
                        blockedUsersConcatenated.append(id).append(",");
                    }
                    blockedUsersConcatenated.deleteCharAt(blockedUsersConcatenated.length() - 1);
                }
            }
        }

        return eventRepository.findAll(search, eventTypes, maxParticipants, location, startDate, endDate, PrivacyType.PUBLIC, blockedUsersConcatenated.toString(), pageable);
    }
  
    public Page<Event> getEventsByUserId(Long userId, String search, ArrayList<String> eventTypes, Integer maxParticipants, String location, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return eventRepository.findAllByUserId(userId, search, eventTypes, maxParticipants, location, startDate, endDate, pageable);
    }
  
    public Page<Event> getPublicEvents(String search, ArrayList<String> eventTypes, Integer maxParticipants, String location, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return eventRepository.findAllPublic(search, eventTypes, maxParticipants, location, startDate, endDate, pageable);
    }

    public Page<Event> getPublicEventsForUser(String search, ArrayList<String> eventTypes, Integer maxParticipants, String location, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable, User user) {
        return eventRepository.findAllPublicForUser(search, eventTypes, maxParticipants, location, startDate, endDate, user, pageable);
    }

    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    public ArrayList<Event> getFeaturedEvents(User user) {
        Pageable pageable = PageRequest.of(0, 5);

        StringBuilder blockedUsersConcatenated = new StringBuilder();

        if (user != null) {
            List<User> blockedUsers = user.getBlocked();
            if (blockedUsers != null && !blockedUsers.isEmpty()) {
                for (User blockedUser : blockedUsers) {
                    blockedUsersConcatenated.append(blockedUser.getId()).append(",");
                }
                blockedUsersConcatenated.deleteCharAt(blockedUsersConcatenated.length() - 1);
            }

            if (user.getRole().getName().equals("ROLE_AuthenticatedUser")) {
                List<Long> organizerIdsWhoBlockedUser = eventRepository.findAllOrganizerIdsWhoBlockedUserId(user.getId());
                if (organizerIdsWhoBlockedUser != null && !organizerIdsWhoBlockedUser.isEmpty()) {
                    for (Long id : organizerIdsWhoBlockedUser) {
                        blockedUsersConcatenated.append(id).append(",");
                    }
                    blockedUsersConcatenated.deleteCharAt(blockedUsersConcatenated.length() - 1);
                }
            }
        }

        return eventRepository.findFeaturedEvents(PrivacyType.PUBLIC, blockedUsersConcatenated.toString(), pageable);
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

    public ArrayList<String> getAllUniqueEventTypesForEvents() {
        return eventRepository.findAllUniqueEventTypeNamesForEvents();
    }

    public ArrayList<String> getAllUniqueLocationsForEvents() {
        return eventRepository.findAllUniqueLocationNamesForEvents();
    }

    public List<User> getAttendingUsersByEvent(Long eventId) {
        return eventRepository.findAttendingUsersByEvent(eventId);
    }
  
    public ArrayList<Event> getUnreviewedAcceptedEventsByUserId(Long userId) {
        LocalDateTime dateNowMinusOne = LocalDateTime.now().minusDays(1);
        return eventRepository.findUnreviewedAcceptedEvents(userId, dateNowMinusOne);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
