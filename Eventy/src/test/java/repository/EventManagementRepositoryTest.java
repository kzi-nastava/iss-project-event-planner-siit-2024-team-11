package repository;

import jakarta.validation.ConstraintViolationException;
import org.example.eventy.EventyApplication;
import org.example.eventy.events.models.*;
import org.example.eventy.events.repositories.EventRepository;
import org.example.eventy.events.repositories.EventTypeRepository;
import org.example.eventy.users.models.EventOrganizer;
import org.example.eventy.users.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EventyApplication.class) // loads full Spring context
@ActiveProfiles("test") // uses test database
@Transactional // DB is cleaned up after each test
public class EventManagementRepositoryTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventTypeRepository eventTypeRepository;
    @Autowired
    private UserRepository userRepository;

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

    @Test
    void saveEvent_ValidInput_ReturnsSavedEvent() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());
        
        Event savedEvent = eventRepository.save(event);

        assertThat(savedEvent.getId()).isNotNull();
        assertThat(savedEvent.getName()).isEqualTo(event.getName());
        assertThat(savedEvent.getDescription()).isEqualTo(event.getDescription());
        assertThat(savedEvent.getMaxNumberParticipants()).isEqualTo(event.getMaxNumberParticipants());
        assertThat(savedEvent.getPrivacy()).isEqualTo(event.getPrivacy());
        assertThat(savedEvent.getDate()).isEqualTo(event.getDate());
        assertThat(savedEvent.getType().getName()).isEqualTo(event.getType().getName());
        assertThat(savedEvent.getType().getDescription()).isEqualTo(event.getType().getDescription());
        assertThat(savedEvent.getType().isActive()).isEqualTo(event.getType().isActive());
        assertThat(savedEvent.getType().getRecommendedSolutionCategories()).isEqualTo(event.getType().getRecommendedSolutionCategories());
        assertThat(savedEvent.getLocation().getId()).isNotNull();
        assertThat(savedEvent.getLocation().getName()).isEqualTo(event.getLocation().getName());
        assertThat(savedEvent.getLocation().getAddress()).isEqualTo(event.getLocation().getAddress());
        assertThat(savedEvent.getLocation().getLatitude()).isEqualTo(event.getLocation().getLatitude());
        assertThat(savedEvent.getLocation().getLongitude()).isEqualTo(event.getLocation().getLongitude());
        assertThat(savedEvent.getAgenda()).isEqualTo(event.getAgenda());
        assertThat(savedEvent.getAgenda().get(0).getId()).isNotNull();
        assertThat(savedEvent.getInvitations().size()).isEqualTo(0);
    }

    @Test
    void saveEvent_NameNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName(null);
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_DescriptionNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription(null);
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_MaxNumberParticipantsNegative_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(-1);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_MaxNumberParticipantsZero_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(0);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_PrivacyTypeNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(null);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_PrivacyTypePrivate_ReturnsSavedEvent() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PRIVATE);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        Event savedEvent = eventRepository.save(event);

        assertThat(savedEvent.getId()).isNotNull();
        assertThat(savedEvent.getName()).isEqualTo(event.getName());
        assertThat(savedEvent.getDescription()).isEqualTo(event.getDescription());
        assertThat(savedEvent.getMaxNumberParticipants()).isEqualTo(event.getMaxNumberParticipants());
        assertThat(savedEvent.getPrivacy()).isEqualTo(event.getPrivacy());
        assertThat(savedEvent.getDate()).isEqualTo(event.getDate());
        assertThat(savedEvent.getType().getName()).isEqualTo(event.getType().getName());
        assertThat(savedEvent.getType().getDescription()).isEqualTo(event.getType().getDescription());
        assertThat(savedEvent.getType().isActive()).isEqualTo(event.getType().isActive());
        assertThat(savedEvent.getType().getRecommendedSolutionCategories()).isEqualTo(event.getType().getRecommendedSolutionCategories());
        assertThat(savedEvent.getLocation().getId()).isNotNull();
        assertThat(savedEvent.getLocation().getName()).isEqualTo(event.getLocation().getName());
        assertThat(savedEvent.getLocation().getAddress()).isEqualTo(event.getLocation().getAddress());
        assertThat(savedEvent.getLocation().getLatitude()).isEqualTo(event.getLocation().getLatitude());
        assertThat(savedEvent.getLocation().getLongitude()).isEqualTo(event.getLocation().getLongitude());
        assertThat(savedEvent.getAgenda()).isEqualTo(event.getAgenda());
        assertThat(savedEvent.getAgenda().get(0).getId()).isNotNull();
        assertThat(savedEvent.getInvitations().size()).isEqualTo(0);
    }

    @Test
    void saveEvent_DateNotGiven_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().minusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_PastDate_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().minusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_DateRightNow_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now());
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_EventTypeNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        event.setType(null);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_LocationNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        event.setLocation(null);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_LocationNameNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName(null);
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_LocationAddressNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress(null);
        location.setLatitude(10.23);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_LocationLatitudeLessThanPossible_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(-90.25);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_LocationLatitudeMoreThanPossible_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(90.25);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_LocationLatitudeLowerBound_ReturnsSavedEvent() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(-90);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        Event savedEvent = eventRepository.save(event);

        assertThat(savedEvent.getId()).isNotNull();
        assertThat(savedEvent.getName()).isEqualTo(event.getName());
        assertThat(savedEvent.getDescription()).isEqualTo(event.getDescription());
        assertThat(savedEvent.getMaxNumberParticipants()).isEqualTo(event.getMaxNumberParticipants());
        assertThat(savedEvent.getPrivacy()).isEqualTo(event.getPrivacy());
        assertThat(savedEvent.getDate()).isEqualTo(event.getDate());
        assertThat(savedEvent.getType().getName()).isEqualTo(event.getType().getName());
        assertThat(savedEvent.getType().getDescription()).isEqualTo(event.getType().getDescription());
        assertThat(savedEvent.getType().isActive()).isEqualTo(event.getType().isActive());
        assertThat(savedEvent.getType().getRecommendedSolutionCategories()).isEqualTo(event.getType().getRecommendedSolutionCategories());
        assertThat(savedEvent.getLocation().getId()).isNotNull();
        assertThat(savedEvent.getLocation().getName()).isEqualTo(event.getLocation().getName());
        assertThat(savedEvent.getLocation().getAddress()).isEqualTo(event.getLocation().getAddress());
        assertThat(savedEvent.getLocation().getLatitude()).isEqualTo(event.getLocation().getLatitude());
        assertThat(savedEvent.getLocation().getLongitude()).isEqualTo(event.getLocation().getLongitude());
        assertThat(savedEvent.getAgenda()).isEqualTo(event.getAgenda());
        assertThat(savedEvent.getAgenda().get(0).getId()).isNotNull();
        assertThat(savedEvent.getInvitations().size()).isEqualTo(0);
    }

    @Test
    void saveEvent_LocationLatitudeUpperBound_ReturnsSavedEvent() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(90);
        location.setLongitude(10.23);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        Event savedEvent = eventRepository.save(event);

        assertThat(savedEvent.getId()).isNotNull();
        assertThat(savedEvent.getName()).isEqualTo(event.getName());
        assertThat(savedEvent.getDescription()).isEqualTo(event.getDescription());
        assertThat(savedEvent.getMaxNumberParticipants()).isEqualTo(event.getMaxNumberParticipants());
        assertThat(savedEvent.getPrivacy()).isEqualTo(event.getPrivacy());
        assertThat(savedEvent.getDate()).isEqualTo(event.getDate());
        assertThat(savedEvent.getType().getName()).isEqualTo(event.getType().getName());
        assertThat(savedEvent.getType().getDescription()).isEqualTo(event.getType().getDescription());
        assertThat(savedEvent.getType().isActive()).isEqualTo(event.getType().isActive());
        assertThat(savedEvent.getType().getRecommendedSolutionCategories()).isEqualTo(event.getType().getRecommendedSolutionCategories());
        assertThat(savedEvent.getLocation().getId()).isNotNull();
        assertThat(savedEvent.getLocation().getName()).isEqualTo(event.getLocation().getName());
        assertThat(savedEvent.getLocation().getAddress()).isEqualTo(event.getLocation().getAddress());
        assertThat(savedEvent.getLocation().getLatitude()).isEqualTo(event.getLocation().getLatitude());
        assertThat(savedEvent.getLocation().getLongitude()).isEqualTo(event.getLocation().getLongitude());
        assertThat(savedEvent.getAgenda()).isEqualTo(event.getAgenda());
        assertThat(savedEvent.getAgenda().get(0).getId()).isNotNull();
        assertThat(savedEvent.getInvitations().size()).isEqualTo(0);
    }

    @Test
    void saveEvent_LocationLongitudeLessThanPossible_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(-180.25);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_LocationLongitudeMoreThanPossible_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(180.25);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_LocationLongitudeLowerBound_ReturnsSavedEvent() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(-180);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        Event savedEvent = eventRepository.save(event);

        assertThat(savedEvent.getId()).isNotNull();
        assertThat(savedEvent.getName()).isEqualTo(event.getName());
        assertThat(savedEvent.getDescription()).isEqualTo(event.getDescription());
        assertThat(savedEvent.getMaxNumberParticipants()).isEqualTo(event.getMaxNumberParticipants());
        assertThat(savedEvent.getPrivacy()).isEqualTo(event.getPrivacy());
        assertThat(savedEvent.getDate()).isEqualTo(event.getDate());
        assertThat(savedEvent.getType().getName()).isEqualTo(event.getType().getName());
        assertThat(savedEvent.getType().getDescription()).isEqualTo(event.getType().getDescription());
        assertThat(savedEvent.getType().isActive()).isEqualTo(event.getType().isActive());
        assertThat(savedEvent.getType().getRecommendedSolutionCategories()).isEqualTo(event.getType().getRecommendedSolutionCategories());
        assertThat(savedEvent.getLocation().getId()).isNotNull();
        assertThat(savedEvent.getLocation().getName()).isEqualTo(event.getLocation().getName());
        assertThat(savedEvent.getLocation().getAddress()).isEqualTo(event.getLocation().getAddress());
        assertThat(savedEvent.getLocation().getLatitude()).isEqualTo(event.getLocation().getLatitude());
        assertThat(savedEvent.getLocation().getLongitude()).isEqualTo(event.getLocation().getLongitude());
        assertThat(savedEvent.getAgenda()).isEqualTo(event.getAgenda());
        assertThat(savedEvent.getAgenda().get(0).getId()).isNotNull();
        assertThat(savedEvent.getInvitations().size()).isEqualTo(0);
    }

    @Test
    void saveEvent_LocationLongitudeUpperBound_ReturnsSavedEvent() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(180);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        Event savedEvent = eventRepository.save(event);

        assertThat(savedEvent.getId()).isNotNull();
        assertThat(savedEvent.getName()).isEqualTo(event.getName());
        assertThat(savedEvent.getDescription()).isEqualTo(event.getDescription());
        assertThat(savedEvent.getMaxNumberParticipants()).isEqualTo(event.getMaxNumberParticipants());
        assertThat(savedEvent.getPrivacy()).isEqualTo(event.getPrivacy());
        assertThat(savedEvent.getDate()).isEqualTo(event.getDate());
        assertThat(savedEvent.getType().getName()).isEqualTo(event.getType().getName());
        assertThat(savedEvent.getType().getDescription()).isEqualTo(event.getType().getDescription());
        assertThat(savedEvent.getType().isActive()).isEqualTo(event.getType().isActive());
        assertThat(savedEvent.getType().getRecommendedSolutionCategories()).isEqualTo(event.getType().getRecommendedSolutionCategories());
        assertThat(savedEvent.getLocation().getId()).isNotNull();
        assertThat(savedEvent.getLocation().getName()).isEqualTo(event.getLocation().getName());
        assertThat(savedEvent.getLocation().getAddress()).isEqualTo(event.getLocation().getAddress());
        assertThat(savedEvent.getLocation().getLatitude()).isEqualTo(event.getLocation().getLatitude());
        assertThat(savedEvent.getLocation().getLongitude()).isEqualTo(event.getLocation().getLongitude());
        assertThat(savedEvent.getAgenda()).isEqualTo(event.getAgenda());
        assertThat(savedEvent.getAgenda().get(0).getId()).isNotNull();
        assertThat(savedEvent.getInvitations().size()).isEqualTo(0);
    }

    @Test
    void saveEvent_ActivityNameNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.24);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName(null);
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_ActivityDescriptionNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.24);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription(null);
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_ActivityStartTimeNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.24);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(null);
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_ActivityPastStartTime_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.24);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().minusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_ActivityStartTimeRightNow_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.24);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now());
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_ActivityEndTimeNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.24);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(null);
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_ActivityPastEndTime_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.24);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().minusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_ActivityEndTimeRightNow_ThrowsConstraintViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.24);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now());
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(ConstraintViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_ActivityLocationNotGiven_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.24);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation(null);
        event.setAgenda(List.of(activity));
        event.setOrganiser((EventOrganizer) userRepository.findByEmail("ves@gmail.com"));
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }

    @Test
    void saveEvent_OrganizerNotSet_ThrowsDataIntegrityViolationException() {
        Event event = new Event();
        event.setName("Annual Tech Conference");
        event.setDescription("A conference providing insights into future tech.");
        event.setMaxNumberParticipants(100);
        event.setPrivacy(PrivacyType.PUBLIC);
        event.setDate(LocalDateTime.now().plusDays(30));
        EventType eventType = new EventType();
        eventType.setId(0L);
        eventType.setName("All");
        eventType.setDescription("ALL");
        eventType.setActive(true);
        eventType.setRecommendedSolutionCategories(new HashSet<>());
        event.setType(eventType);
        Location location = new Location();
        location.setName("123 Street");
        location.setAddress("City");
        location.setLatitude(10.23);
        location.setLongitude(10.24);
        event.setLocation(location);
        Activity activity = new Activity();
        activity.setName("Activity 1");
        activity.setDescription("Activity description");
        activity.setStartTime(LocalDateTime.now().plusHours(25));
        activity.setEndTime(LocalDateTime.now().plusHours(27));
        activity.setLocation("Activity Location");
        event.setAgenda(List.of(activity));
        event.setOrganiser(null);
        event.setInvitations(new ArrayList<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            eventRepository.save(event);
        });
    }
}
