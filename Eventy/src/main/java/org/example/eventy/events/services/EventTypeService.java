package org.example.eventy.events.services;

import org.example.eventy.events.models.Event;
import org.example.eventy.events.models.EventType;
import org.example.eventy.events.repositories.EventTypeRepository;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventTypeService {
    @Autowired
    private EventTypeRepository eventTypeRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private SolutionService solutionService;

    public List<EventType> getTypes(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return eventTypeRepository.findAll(pageable).getContent();
        }

        return eventTypeRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable).getContent();
    }

    public List<EventType> getActiveTypes() {
        return eventTypeRepository.findByIsActiveTrue();
    }

    public EventType get(Long eventTypeId) {
        return eventTypeRepository.findById(eventTypeId).orElse(null);
    }

    public EventType save(EventType eventType) {
        try {
            return eventTypeRepository.save(eventType);
        }
        catch (Exception e) {
            return null;
        }
    }

    public EventType toggleActivation(Long eventTypeId) {
        EventType eventType = eventTypeRepository.findById(eventTypeId).orElse(null);

        if(eventType != null) {
            if (eventType.isActive()) {
                for(Event event : eventService.getAllEvents()) {
                    if (event.getType().equals(eventType) && event.getDate().isAfter(LocalDateTime.now())) {
                        return null;
                    }
                }

                for(Solution solution : solutionService.getAllSolutions()) {
                    if(solution.getEventTypes().contains(eventType)) {
                        return null;
                    }
                }
            }

            eventType.setActive(!eventType.isActive());
            eventTypeRepository.save(eventType);
        }

        return eventType;
    }

    public long getCount(String search) {
        return eventTypeRepository.findCountSearch(search);
    }
}
