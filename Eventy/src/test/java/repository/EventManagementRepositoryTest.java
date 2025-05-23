package repository;

import org.example.eventy.EventyApplication;
import org.example.eventy.events.models.EventType;
import org.example.eventy.events.repositories.EventRepository;
import org.example.eventy.events.repositories.EventTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EventyApplication.class) // loads full Spring context
@ActiveProfiles("test") // uses test database
@Transactional // DB is cleaned up after each test
public class EventManagementRepositoryTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Test
    public void getActiveEventTypes_HasActiveEventTypes_ReturnsEventTypes() {
        for(EventType eventType : eventTypeRepository.findAll()) {
            eventType.setActive(false);
            eventTypeRepository.save(eventType);
        }
        EventType eventType1 = new EventType(eventTypeRepository.count() + 10, "Type 1", "Desc Type 1", true, new HashSet<>());
        EventType eventType2 = new EventType(eventTypeRepository.count() + 111, "Type 2", "Desc Type 2", true, new HashSet<>());
        eventTypeRepository.save(eventType1);
        eventTypeRepository.save(eventType2);
        List<EventType> eventTypes = eventTypeRepository.findByIsActiveTrue();

        assertNotNull(eventTypes);
        assertEquals(2, eventTypes.size());
        EventType repoEventType1 = eventTypes.get(0);
        EventType repoEventType2 = eventTypes.get(1);
        assertNotNull(repoEventType1.getId());
        assertEquals(repoEventType1.getName(), eventType1.getName());
        assertEquals(repoEventType1.getDescription(), eventType1.getDescription());
        assertEquals(repoEventType1.getRecommendedSolutionCategories(), eventType1.getRecommendedSolutionCategories());
        assertNotNull(repoEventType2.getId());
        assertEquals(repoEventType2.getName(), eventType2.getName());
        assertEquals(repoEventType2.getDescription(), eventType2.getDescription());
        assertEquals(repoEventType2.getRecommendedSolutionCategories(), eventType2.getRecommendedSolutionCategories());
    }

    @Test
    public void getActiveEventTypes_HasNoActiveEventTypes_ReturnsEmptyList() {
        for(EventType eventType : eventTypeRepository.findAll()) {
            eventType.setActive(false);
            eventTypeRepository.save(eventType);
        }

        List<EventType> eventTypes = eventTypeRepository.findByIsActiveTrue();

        assertNotNull(eventTypes);
        assertTrue(eventTypes.isEmpty());
    }
}
