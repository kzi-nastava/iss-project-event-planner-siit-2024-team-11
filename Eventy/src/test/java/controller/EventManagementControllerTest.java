package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.example.eventy.EventyApplication;
import org.example.eventy.events.dtos.CreateActivityDTO;
import org.example.eventy.events.dtos.CreateLocationDTO;
import org.example.eventy.events.dtos.EventTypeCardDTO;
import org.example.eventy.events.dtos.OrganizeEventDTO;
import org.example.eventy.events.models.EventType;
import org.example.eventy.events.services.EventService;
import org.example.eventy.events.services.EventTypeService;
import org.example.eventy.users.dtos.LoginDTO;
import org.example.eventy.users.dtos.UserTokenState;
import org.example.eventy.util.TokenUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EventyApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test") // uses test database
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Ensures @BeforeAll is not static
public class EventManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenUtils tokenUtils;
    private String jwtToken;

    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private EventService eventService;

    @BeforeAll
    void setupTestData() throws Exception {
        login("ves@gmail.com");
    }

    @AfterAll
    void tearDownTestData() throws Exception {
        logout();
    }

    private void login(String email) throws Exception {
        LoginDTO loginDTO = new LoginDTO(email, "admin");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        UserTokenState tokenState = objectMapper.readValue(responseContent, UserTokenState.class);
        jwtToken = tokenState.getAccessToken();
    }

    private void logout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/authentication/logout")
                        .header("Authorization", "Bearer " + jwtToken)  // Include token
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        jwtToken = null;
    }

    @Test
    @Transactional
    public void createEvent_ValidInput_ReturnsCreatedEvent() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(27)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newEvent.getName()))
                .andExpect(jsonPath("$.description").value(newEvent.getDescription()))
                .andExpect(jsonPath("$.maxNumberParticipants").value(newEvent.getMaxNumberParticipants()))
                .andExpect(jsonPath("$.public").value(newEvent.getIsPublic()))
                .andExpect(jsonPath("$.eventType.id").value(newEvent.getEventTypeId()))
                .andExpect(jsonPath("$.location.address").value(newEvent.getLocation().getAddress()))
                .andExpect(jsonPath("$.location.name").value(newEvent.getLocation().getName()))
                .andExpect(jsonPath("$.location.latitude").value(newEvent.getLocation().getLatitude()))
                .andExpect(jsonPath("$.location.longitude").value(newEvent.getLocation().getLongitude()))
                .andExpect(jsonPath("$.date").value(newEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda").isArray())
                .andExpect(jsonPath("$.agenda[0].name").value(newEvent.getAgenda().get(0).getName()))
                .andExpect(jsonPath("$.agenda[0].description").value(newEvent.getAgenda().get(0).getDescription()))
                .andExpect(jsonPath("$.agenda[0].location").value(newEvent.getAgenda().get(0).getLocation()))
                .andExpect(jsonPath("$.agenda[0].startTime").value(newEvent.getAgenda().get(0).getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda[0].endTime").value(newEvent.getAgenda().get(0).getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.organizerId").value(newEvent.getOrganizerId()));
    }

    @Test
    @Transactional
    public void createEvent_InvalidOrganizerID_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken) + 2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid Organizer ID!"));
    }

    @Test
    @Transactional
    public void createEvent_UserNotOrganizer_ReturnsForbidden() throws Exception {
        logout();
        login("provider@gmail.com");

        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isForbidden());

        logout();
        login("ves@gmail.com");
    }

    @Test
    @Transactional
    public void createEvent_UserNotAuthenticated_ReturnsUnauthorized() throws Exception {
        logout();

        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isUnauthorized());

        login("ves@gmail.com");
    }

    @Test
    @Transactional
    public void createEvent_EventTypeNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(null);
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("eventTypeId: Event type cannot be null"));
    }

    @Test
    @Transactional
    public void createEvent_EventTypeDoesntExist_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId((long) eventTypeService.getActiveTypes().size() + 5);
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(27)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Event type doesn't exist!"));
    }

    @Test
    @Transactional
    public void createEvent_ValidInputWithEventTypeAll_ReturnsCreatedEvent() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(0).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(27)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newEvent.getName()))
                .andExpect(jsonPath("$.description").value(newEvent.getDescription()))
                .andExpect(jsonPath("$.maxNumberParticipants").value(newEvent.getMaxNumberParticipants()))
                .andExpect(jsonPath("$.public").value(newEvent.getIsPublic()))
                .andExpect(jsonPath("$.eventType.id").value(newEvent.getEventTypeId()))
                .andExpect(jsonPath("$.location.address").value(newEvent.getLocation().getAddress()))
                .andExpect(jsonPath("$.location.name").value(newEvent.getLocation().getName()))
                .andExpect(jsonPath("$.location.latitude").value(newEvent.getLocation().getLatitude()))
                .andExpect(jsonPath("$.location.longitude").value(newEvent.getLocation().getLongitude()))
                .andExpect(jsonPath("$.date").value(newEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda").isArray())
                .andExpect(jsonPath("$.agenda[0].name").value(newEvent.getAgenda().get(0).getName()))
                .andExpect(jsonPath("$.agenda[0].description").value(newEvent.getAgenda().get(0).getDescription()))
                .andExpect(jsonPath("$.agenda[0].location").value(newEvent.getAgenda().get(0).getLocation()))
                .andExpect(jsonPath("$.agenda[0].startTime").value(newEvent.getAgenda().get(0).getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda[0].endTime").value(newEvent.getAgenda().get(0).getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.organizerId").value(newEvent.getOrganizerId()));
    }

    @Test
    @Transactional
    public void createEvent_NameNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName(null);
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("name: Name cannot be empty"));
    }

    @Test
    @Transactional
    public void createEvent_NameEmpty_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("name: Name cannot be empty"));
    }

    @Test
    @Transactional
    public void createEvent_DescriptionNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription(null);
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("description: Description cannot be empty"));
    }

    @Test
    @Transactional
    public void createEvent_DescriptionEmpty_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("description: Description cannot be empty"));
    }

    @Test
    @Transactional
    public void createEvent_MaxNumberParticipantsNegative_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(-1);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("maxNumberParticipants: Number of participants must be positive"));
    }

    @Test
    @Transactional
    public void createEvent_MaxNumberParticipantsZero_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(0);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("maxNumberParticipants: Number of participants must be positive"));
    }

    @Test
    @Transactional
    public void createEvent_PrivateEvent_ReturnsCreatedEvent() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(false);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(0).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(27)
                )
        ));
        newEvent.setEmails(List.of("veselin.roganovic.rogan003@gmail.com"));
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newEvent.getName()))
                .andExpect(jsonPath("$.description").value(newEvent.getDescription()))
                .andExpect(jsonPath("$.maxNumberParticipants").value(newEvent.getMaxNumberParticipants()))
                .andExpect(jsonPath("$.public").value(newEvent.getIsPublic()))
                .andExpect(jsonPath("$.eventType.id").value(newEvent.getEventTypeId()))
                .andExpect(jsonPath("$.location.address").value(newEvent.getLocation().getAddress()))
                .andExpect(jsonPath("$.location.name").value(newEvent.getLocation().getName()))
                .andExpect(jsonPath("$.location.latitude").value(newEvent.getLocation().getLatitude()))
                .andExpect(jsonPath("$.location.longitude").value(newEvent.getLocation().getLongitude()))
                .andExpect(jsonPath("$.date").value(newEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda").isArray())
                .andExpect(jsonPath("$.agenda[0].name").value(newEvent.getAgenda().get(0).getName()))
                .andExpect(jsonPath("$.agenda[0].description").value(newEvent.getAgenda().get(0).getDescription()))
                .andExpect(jsonPath("$.agenda[0].location").value(newEvent.getAgenda().get(0).getLocation()))
                .andExpect(jsonPath("$.agenda[0].startTime").value(newEvent.getAgenda().get(0).getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda[0].endTime").value(newEvent.getAgenda().get(0).getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.organizerId").value(newEvent.getOrganizerId()));
    }

    @Test
    @Transactional
    public void createEvent_DateNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(null);
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("date: Date cannot be null"));
    }

    @Test
    @Transactional
    public void createEvent_DateInThePast_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().minusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("date: Date must be in the future"));
    }

    @Test
    @Transactional
    public void createEvent_DateRightNow_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now());
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("date: Date must be in the future"));
    }

    @Test
    @Transactional
    public void createEvent_LocationNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(null);
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("location: Location cannot be null"));
    }

    @Test
    @Transactional
    public void createEvent_LocationNameNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO(null, "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("location.name: Name cannot be empty"));
    }

    @Test
    @Transactional
    public void createEvent_LocationNameEmpty_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("", "City", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("location.name: Name cannot be empty"));
    }

    @Test
    @Transactional
    public void createEvent_LocationAddressNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", null, 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("location.address: Address cannot be empty"));
    }

    @Test
    @Transactional
    public void createEvent_LocationAddressEmpty_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("location.address: Address cannot be empty"));
    }

    @Test
    @Transactional
    public void createEvent_LocationLatitudeLessThanPossible_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", -90.25, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("location.latitude: Latitude must be between -90 and 90"));
    }

    @Test
    @Transactional
    public void createEvent_LocationLatitudeMoreThanPossible_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 90.25, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("location.latitude: Latitude must be between -90 and 90"));
    }

    @Test
    @Transactional
    public void createEvent_LocationLatitudeEqualsLowerBound_ReturnsCreatedEvent() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", -90, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(27)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newEvent.getName()))
                .andExpect(jsonPath("$.description").value(newEvent.getDescription()))
                .andExpect(jsonPath("$.maxNumberParticipants").value(newEvent.getMaxNumberParticipants()))
                .andExpect(jsonPath("$.public").value(newEvent.getIsPublic()))
                .andExpect(jsonPath("$.eventType.id").value(newEvent.getEventTypeId()))
                .andExpect(jsonPath("$.location.address").value(newEvent.getLocation().getAddress()))
                .andExpect(jsonPath("$.location.name").value(newEvent.getLocation().getName()))
                .andExpect(jsonPath("$.location.latitude").value(newEvent.getLocation().getLatitude()))
                .andExpect(jsonPath("$.location.longitude").value(newEvent.getLocation().getLongitude()))
                .andExpect(jsonPath("$.date").value(newEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda").isArray())
                .andExpect(jsonPath("$.agenda[0].name").value(newEvent.getAgenda().get(0).getName()))
                .andExpect(jsonPath("$.agenda[0].description").value(newEvent.getAgenda().get(0).getDescription()))
                .andExpect(jsonPath("$.agenda[0].location").value(newEvent.getAgenda().get(0).getLocation()))
                .andExpect(jsonPath("$.agenda[0].startTime").value(newEvent.getAgenda().get(0).getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda[0].endTime").value(newEvent.getAgenda().get(0).getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.organizerId").value(newEvent.getOrganizerId()));
    }

    @Test
    @Transactional
    public void createEvent_LocationLatitudeEqualsUpperBound_ReturnsCreatedEvent() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 90, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(27)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newEvent.getName()))
                .andExpect(jsonPath("$.description").value(newEvent.getDescription()))
                .andExpect(jsonPath("$.maxNumberParticipants").value(newEvent.getMaxNumberParticipants()))
                .andExpect(jsonPath("$.public").value(newEvent.getIsPublic()))
                .andExpect(jsonPath("$.eventType.id").value(newEvent.getEventTypeId()))
                .andExpect(jsonPath("$.location.address").value(newEvent.getLocation().getAddress()))
                .andExpect(jsonPath("$.location.name").value(newEvent.getLocation().getName()))
                .andExpect(jsonPath("$.location.latitude").value(newEvent.getLocation().getLatitude()))
                .andExpect(jsonPath("$.location.longitude").value(newEvent.getLocation().getLongitude()))
                .andExpect(jsonPath("$.date").value(newEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda").isArray())
                .andExpect(jsonPath("$.agenda[0].name").value(newEvent.getAgenda().get(0).getName()))
                .andExpect(jsonPath("$.agenda[0].description").value(newEvent.getAgenda().get(0).getDescription()))
                .andExpect(jsonPath("$.agenda[0].location").value(newEvent.getAgenda().get(0).getLocation()))
                .andExpect(jsonPath("$.agenda[0].startTime").value(newEvent.getAgenda().get(0).getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda[0].endTime").value(newEvent.getAgenda().get(0).getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.organizerId").value(newEvent.getOrganizerId()));
    }

    @Test
    @Transactional
    public void createEvent_LocationLongitudeLessThanPossible_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.21, -180.25));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("location.longitude: Longitude must be between -180 and 180"));
    }

    @Test
    @Transactional
    public void createEvent_LocationLongitudeMoreThanPossible_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 180.25));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("location.longitude: Longitude must be between -180 and 180"));
    }

    @Test
    @Transactional
    public void createEvent_LocationLongitudeEqualsLowerBound_ReturnsCreatedEvent() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, -180));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(27)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newEvent.getName()))
                .andExpect(jsonPath("$.description").value(newEvent.getDescription()))
                .andExpect(jsonPath("$.maxNumberParticipants").value(newEvent.getMaxNumberParticipants()))
                .andExpect(jsonPath("$.public").value(newEvent.getIsPublic()))
                .andExpect(jsonPath("$.eventType.id").value(newEvent.getEventTypeId()))
                .andExpect(jsonPath("$.location.address").value(newEvent.getLocation().getAddress()))
                .andExpect(jsonPath("$.location.name").value(newEvent.getLocation().getName()))
                .andExpect(jsonPath("$.location.latitude").value(newEvent.getLocation().getLatitude()))
                .andExpect(jsonPath("$.location.longitude").value(newEvent.getLocation().getLongitude()))
                .andExpect(jsonPath("$.date").value(newEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda").isArray())
                .andExpect(jsonPath("$.agenda[0].name").value(newEvent.getAgenda().get(0).getName()))
                .andExpect(jsonPath("$.agenda[0].description").value(newEvent.getAgenda().get(0).getDescription()))
                .andExpect(jsonPath("$.agenda[0].location").value(newEvent.getAgenda().get(0).getLocation()))
                .andExpect(jsonPath("$.agenda[0].startTime").value(newEvent.getAgenda().get(0).getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda[0].endTime").value(newEvent.getAgenda().get(0).getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.organizerId").value(newEvent.getOrganizerId()));
    }

    @Test
    @Transactional
    public void createEvent_LocationLongitudeEqualsUpperBound_ReturnsCreatedEvent() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "City", 10.23, 180));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(27)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newEvent.getName()))
                .andExpect(jsonPath("$.description").value(newEvent.getDescription()))
                .andExpect(jsonPath("$.maxNumberParticipants").value(newEvent.getMaxNumberParticipants()))
                .andExpect(jsonPath("$.public").value(newEvent.getIsPublic()))
                .andExpect(jsonPath("$.eventType.id").value(newEvent.getEventTypeId()))
                .andExpect(jsonPath("$.location.address").value(newEvent.getLocation().getAddress()))
                .andExpect(jsonPath("$.location.name").value(newEvent.getLocation().getName()))
                .andExpect(jsonPath("$.location.latitude").value(newEvent.getLocation().getLatitude()))
                .andExpect(jsonPath("$.location.longitude").value(newEvent.getLocation().getLongitude()))
                .andExpect(jsonPath("$.date").value(newEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda").isArray())
                .andExpect(jsonPath("$.agenda[0].name").value(newEvent.getAgenda().get(0).getName()))
                .andExpect(jsonPath("$.agenda[0].description").value(newEvent.getAgenda().get(0).getDescription()))
                .andExpect(jsonPath("$.agenda[0].location").value(newEvent.getAgenda().get(0).getLocation()))
                .andExpect(jsonPath("$.agenda[0].startTime").value(newEvent.getAgenda().get(0).getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.agenda[0].endTime").value(newEvent.getAgenda().get(0).getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.organizerId").value(newEvent.getOrganizerId()));
    }

    @Test
    @Transactional
    public void createEvent_AgendaNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(null);
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("agenda: Agenda cannot be empty"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaEmpty_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(new ArrayList<>());
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("agenda: Agenda cannot be empty"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityNameNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        null,
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(26)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("agenda[0].name: Activity name cannot be empty!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityNameEmpty_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(26)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("agenda[0].name: Activity name cannot be empty!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityDescriptionNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        null,
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(26)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("agenda[0].description: Activity description cannot be empty!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityDescriptionEmpty_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(26)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("agenda[0].description: Activity description cannot be empty!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityLocationNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        null,
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(26)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("agenda[0].location: Activity location cannot be empty!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityLocationEmpty_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(26)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("agenda[0].location: Activity location cannot be empty!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityStartTimeNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        null,
                        LocalDateTime.now().plusHours(26)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("agenda[0].startTime: Activity start time cannot be empty!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityEndTimeNotGiven_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        null
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("agenda[0].endTime: Activity end time cannot be empty!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityStartTimeBeforeEvent_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(26)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Agenda timeline is not possible!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityEndTimeAfterEvent_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(25),
                        LocalDateTime.now().plusHours(100)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Agenda timeline is not possible!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityStartTimeAfterEndTime_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        LocalDateTime.now().plusHours(26),
                        LocalDateTime.now().plusHours(25)
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Agenda timeline is not possible!"));
    }

    @Test
    @Transactional
    public void createEvent_AgendaActivityDurationIs0_ReturnsBadRequest() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(eventTypeService.getActiveTypes().get(1).getId());
        newEvent.setLocation(new CreateLocationDTO("123 Street", "Address", 10.23, 10.23));
        newEvent.setDate(LocalDateTime.now().plusDays(1));
        LocalDateTime startTime = LocalDateTime.now().plusHours(26);
        newEvent.setAgenda(List.of(
                new CreateActivityDTO(
                        "Activity 1",
                        "Activity description",
                        "Activity Location",
                        startTime,
                        startTime
                )
        ));
        newEvent.setEmails(new ArrayList<>());
        newEvent.setOrganizerId(tokenUtils.getIdFromToken(jwtToken));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events", newEvent)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Agenda timeline is not possible!"));
    }

    @Test
    public void downloadGuestListPDF_EventDoesntExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/events/pdfs/guest-list/100")
                        .accept(MediaType.APPLICATION_PDF)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void downloadGuestListPDF_HasTwoGuests_ReturnsOk() throws Exception {
        byte[] pdfBytes = mockMvc.perform(MockMvcRequestBuilders.get("/api/events/pdfs/guest-list/1")
                        .accept(MediaType.APPLICATION_PDF)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        try (PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(pdfBytes))) {
            PdfDocument pdfDocument = new PdfDocument(pdfReader);

            String pageContent = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(1));

            assertTrue(pageContent.contains("1. ves@gmail.com"));
            assertTrue(pageContent.contains("2. c.tamara333@gmail.com"));

            assertTrue(pageContent.contains("Guest List for: " + eventService.getEvent(1L).getName()));
            assertFalse(pageContent.contains("3."));
        }
    }

    @Test
    public void downloadGuestListPDF_HasNoGuests_ReturnsOk() throws Exception {
        byte[] pdfBytes = mockMvc.perform(MockMvcRequestBuilders.get("/api/events/pdfs/guest-list/3")
                        .accept(MediaType.APPLICATION_PDF)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        try (PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(pdfBytes))) {
            PdfDocument pdfDocument = new PdfDocument(pdfReader);

            String pageContent = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(1));

            assertTrue(pageContent.contains("Guest List for: " + eventService.getEvent(3L).getName()));
            assertFalse(pageContent.contains("1."));
        }
    }

    @Test
    public void getActiveTypes_FewActiveTypesExist_ReturnsOk() throws Exception {
        String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/events/types/active")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<EventTypeCardDTO> activeEventTypeCards = objectMapper.readValue(responseContent, new TypeReference<List<EventTypeCardDTO>>() {
        });

        List<EventType> activeEventTypes = eventTypeService.getActiveTypes();
        assertEquals(activeEventTypeCards.size(), activeEventTypes.size());

        for(int i = 0; i < activeEventTypes.size(); i++) {
            assertEquals(activeEventTypes.get(i).getId(), activeEventTypeCards.get(i).getId());
            assertEquals(activeEventTypes.get(i).getName(), activeEventTypeCards.get(i).getName());
        }
    }
}
