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

    public EventType get(Long eventTypeId) {
        return eventTypeRepository.findById(eventTypeId).orElse(null);
    }

    public EventType save(EventType eventType) {
        return eventTypeRepository.save(eventType);
    }

    public boolean deactivate(Long eventTypeId) {
        EventType eventType = eventTypeRepository.findById(eventTypeId).orElse(null);

        if(eventType != null) {
            eventType.setActive(false);
            eventTypeRepository.save(eventType);
            return true;
        }

        return false;
    }
}
