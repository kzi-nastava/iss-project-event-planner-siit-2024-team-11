package org.example.eventy.events.services;

import org.example.eventy.events.dtos.EventCardDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class EventService {

    /*@Autowired
    private EventRepository eventRepository;*/

    public ArrayList<EventCardDTO> getEvents(Pageable pageable) {
        EventCardDTO eventCard1 = new EventCardDTO(
            1L,
            "Spring Tech Summit 2024",
            "A gathering of tech enthusiasts and professionals to explore the latest trends.",
            1000,
            true,
            "Technology",
            "TechHub Conference Center",
            LocalDateTime.of(2024, 5, 10, 9, 0),
            LocalDateTime.of(2024, 5, 10, 18, 0),
            51L,
            "TechWorld Organizers",
            "https://example.com/images/techworld.png"
        );

        EventCardDTO eventCard2 = new EventCardDTO(
            2L,
            "Morning Yoga Retreat",
            "A serene yoga session for relaxation and mindfulness.",
            50,
            true,
            "Wellness",
            "Sunrise Yoga Studio",
            LocalDateTime.of(2024, 6, 15, 7, 30),
            LocalDateTime.of(2024, 6, 15, 9, 0),
            72L,
            "HealthFirst",
            "https://example.com/images/healthfirst.png"
        );

        ArrayList<EventCardDTO> events = new ArrayList<>();
        events.add(eventCard1);
        events.add(eventCard2);

        return events;
    }

    public EventCardDTO getEventCard(Long eventId) {
        EventCardDTO eventCard = new EventCardDTO(
            eventId,
            "Spring Tech Summit 2024",
            "A gathering of tech enthusiasts and professionals to explore the latest trends.",
            1000,
            true,
            "Technology",
            "TechHub Conference Center",
            LocalDateTime.of(2024, 5, 10, 9, 0),
            LocalDateTime.of(2024, 5, 10, 18, 0),
            51L,
            "TechWorld Organizers",
            "https://example.com/images/techworld.png"
        );

        return eventCard;
    }

    public ArrayList<EventCardDTO> getFeaturedEvents() {
        EventCardDTO featuredEventCard1 = new EventCardDTO(
            1L,
            "FEATURED - Spring Tech Summit 2024",
            "A gathering of tech enthusiasts and professionals to explore the latest trends.",
            1000,
            true,
            "Technology",
            "TechHub Conference Center",
            LocalDateTime.of(2024, 5, 10, 9, 0),
            LocalDateTime.of(2024, 5, 10, 18, 0),
            51L,
            "TechWorld Organizers",
            "https://example.com/images/techworld.png"
        );

        EventCardDTO featuredEventCard2 = new EventCardDTO(
            2L,
            "FEATURED - Morning Yoga Retreat",
            "A serene yoga session for relaxation and mindfulness.",
            50,
            true,
            "Wellness",
            "Sunrise Yoga Studio",
            LocalDateTime.of(2024, 6, 15, 7, 30),
            LocalDateTime.of(2024, 6, 15, 9, 0),
            72L,
            "HealthFirst",
            "https://example.com/images/healthfirst.png"
        );

        ArrayList<EventCardDTO> featuredEvents = new ArrayList<>();
        featuredEvents.add(featuredEventCard1);
        featuredEvents.add(featuredEventCard2);

        return featuredEvents;
    }

    public ArrayList<EventCardDTO> filterEvents(String search, ArrayList<String> eventTypes, String location, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        EventCardDTO filteredEventCard1 = new EventCardDTO(
            1L,
            "FILTERED - Spring Tech Summit 2024",
            "A gathering of tech enthusiasts and professionals to explore the latest trends.",
            1000,
            true,
            "Technology",
            "TechHub Conference Center",
            LocalDateTime.of(2024, 5, 10, 9, 0),
            LocalDateTime.of(2024, 5, 10, 18, 0),
            51L,
            "TechWorld Organizers",
            "https://example.com/images/techworld.png"
        );

        EventCardDTO filteredEventCard2 = new EventCardDTO(
            2L,
            "FILTERED - Morning Yoga Retreat",
            "A serene yoga session for relaxation and mindfulness.",
            50,
            true,
            "Wellness",
            "Sunrise Yoga Studio",
            LocalDateTime.of(2024, 6, 15, 7, 30),
            LocalDateTime.of(2024, 6, 15, 9, 0),
            72L,
            "HealthFirst",
            "https://example.com/images/healthfirst.png"
        );

        ArrayList<EventCardDTO> filteredEvents = new ArrayList<>();
        filteredEvents.add(filteredEventCard1);
        filteredEvents.add(filteredEventCard2);

        return filteredEvents;
        //return eventRepository.findFilteredEvents(search, eventTypes, location, startDate, endDate, pageable);
    }
}
