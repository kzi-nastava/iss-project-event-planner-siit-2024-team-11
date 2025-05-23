package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eventy.EventyApplication;
import org.example.eventy.events.dtos.CreateActivityDTO;
import org.example.eventy.events.dtos.CreateLocationDTO;
import org.example.eventy.events.dtos.OrganizeEventDTO;
import org.example.eventy.users.dtos.LoginDTO;
import org.example.eventy.users.dtos.UserTokenState;
import org.example.eventy.util.TokenUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EventyApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test") // uses test database
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Ensures @BeforeAll is not static
public class EventManagementControllerTest {
    private static final Logger log = LoggerFactory.getLogger(EventManagementControllerTest.class);
    // 1. Po tipu eventa, bilo koji, null, all
    // 2. Naziv, opis, maks broj ucesnika, tip privatnosti, lokacija, datum - za vecinu normalna vrednost ili null, ponegde i
    // jos neka logika, npr za broj ucesnika mozemo negativan test za 0 i manje, za datum ne moze proslost i sl
    // 3. Agenda, za naziv, opis, vreme trajanja, lokaciju sve testirati, mozda cak i samu agendu da nije prazna?
    // 4. PDF sa agendom preuzimanje
    // 5. getActiveTypes iz EventTypeController-a treba testirati takodje? dovoljno je praznu listu i
    // da ima 2 elementa i da se lepo prevedu u DTO
    // 6. da ga kreira neko neauth ili neko auth a da nije organizer
    // 7. nesto normalno sto treba da prodje
    // 8. Organizer ID i u jwt-u ne pise isto

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenUtils tokenUtils;
    private String jwtToken;

    @BeforeAll
    void setupTestData() throws Exception {
        login();
    }

    @AfterAll
    void tearDownTestData() throws Exception {
        logout();
    }

    private void login() throws Exception {
        LoginDTO loginDTO = new LoginDTO("ves@gmail.com", "admin");

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
    }

    @Test
    @Transactional
    public void createEvent_ValidInput_ReturnsCreatedEvent() throws Exception {
        OrganizeEventDTO newEvent = new OrganizeEventDTO();
        newEvent.setName("Sample Event");
        newEvent.setDescription("This is a sample event description.");
        newEvent.setMaxNumberParticipants(100);
        newEvent.setIsPublic(true);
        newEvent.setEventTypeId(1L);
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
}
