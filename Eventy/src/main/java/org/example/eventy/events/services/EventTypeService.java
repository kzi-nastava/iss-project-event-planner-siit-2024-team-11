package org.example.eventy.events.services;

import org.example.eventy.events.models.EventType;
import org.example.eventy.events.repositories.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventTypeService {
    @Autowired
    private EventTypeRepository eventTypeRepository;

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
            eventType.setActive(!eventType.isActive());
            eventTypeRepository.save(eventType);
        }

        return eventType;
    }

    public long getCount(String search) {
        return eventTypeRepository.findCountSearch(search);
    }
}
