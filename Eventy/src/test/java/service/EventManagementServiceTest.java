package service;

import org.example.eventy.EventyApplication;
import org.example.eventy.events.models.*;
import org.example.eventy.events.repositories.EventRepository;
import org.example.eventy.events.repositories.EventTypeRepository;
import org.example.eventy.events.services.EventService;
import org.example.eventy.events.services.EventTypeService;
import org.example.eventy.users.models.EventOrganizer;
import org.example.eventy.users.models.User;
import org.example.eventy.users.repositories.UserRepository;
import org.example.eventy.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = EventyApplication.class) // loads full Spring context
@ActiveProfiles("test") // uses test database
public class EventManagementServiceTest {
    @Autowired
    private EventService eventService;
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private UserService userService;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private EventTypeRepository eventTypeRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void createEvent_ValidInput_ReturnsSavedEvent() {
        Event event = new Event();
        Event mockEvent = new Event();

        when(eventRepository.save(event)).thenReturn(mockEvent);

        Event returnedEvent = eventService.save(event);

        assertNotNull(returnedEvent);
        assertEquals(mockEvent, returnedEvent);

        verify(eventRepository, times(1)).save(Mockito.any(Event.class));
    }

    @Test
    public void createEvent_ExceptionHappens_ThrowsException () {
        Event mockEvent = new Event();

        when(eventRepository.save(mockEvent)).thenThrow(new RuntimeException("Database error"));

        Event savedEvent = eventService.save(mockEvent);

        assertNull(savedEvent);
        verify(eventRepository, times(1)).save(mockEvent);
    }

    @Test
    public void getUser_UserExists() {
        User mockUser = new EventOrganizer();
        mockUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User result = userService.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void getUser_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.get(1L);

        assertNull(result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void getActiveTypes_HasActiveTypes() {
        EventType type1 = new EventType();
        type1.setId(1L);
        type1.setName("Conference");
        type1.setActive(true);

        EventType type2 = new EventType();
        type2.setId(2L);
        type2.setName("Workshop");
        type2.setActive(true);

        when(eventTypeRepository.findByIsActiveTrue()).thenReturn(List.of(type1, type2));

        List<EventType> result = eventTypeService.getActiveTypes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(EventType::isActive));
        verify(eventTypeRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    public void testGetActiveTypes_NoActiveTypes() {
        when(eventTypeRepository.findByIsActiveTrue()).thenReturn(Collections.emptyList());

        List<EventType> result = eventTypeService.getActiveTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(eventTypeRepository, times(1)).findByIsActiveTrue();
    }

}
