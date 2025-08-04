package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eventy.EventyApplication;
import org.example.eventy.common.services.EmailService;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.dtos.ReservationDTO;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.services.ReservationService;
import org.example.eventy.solutions.services.SolutionService;
import org.example.eventy.users.dtos.LoginDTO;
import org.example.eventy.users.dtos.UserTokenState;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = EventyApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test") // uses test database
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Ensures @BeforeAll is not static
public class ReservationControllerTest {
    @MockBean
    private EmailService emailService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private EventService eventService;
    @Autowired
    private SolutionService solutionService;
    private String jwtToken;

    @BeforeAll
    void setupTestData() throws Exception {
        login();
        logout();

        Event event1 = eventService.getEvent(1L);
        Event event2 = eventService.getEvent(2L);
        Service service6 = (Service) solutionService.getSolution(6L);
        Service service7 = (Service) solutionService.getSolution(7L);

        List<Reservation> reservations = List.of(
            new Reservation(null, event1, service6, LocalDateTime.of(2027, 6, 10, 10, 0), LocalDateTime.of(2027, 6, 10, 11, 0), false),
            new Reservation(null, event1, service6, LocalDateTime.of(2027, 6, 11, 11, 0), LocalDateTime.of(2027, 6, 11, 12, 0), false),
            new Reservation(null, event1, service6, LocalDateTime.of(2027, 6, 12, 12, 0), LocalDateTime.of(2027, 6, 12, 13, 0), false),
            new Reservation(null, event1, service6, LocalDateTime.of(2027, 6, 13, 13, 0), LocalDateTime.of(2027, 6, 13, 14, 0), false),
            new Reservation(null, event1, service6, LocalDateTime.of(2027, 6, 14, 14, 0), LocalDateTime.of(2027, 6, 14, 15, 0), false),
            new Reservation(null, event2, service7, LocalDateTime.of(2027, 6, 13, 13, 0), LocalDateTime.of(2027, 6, 13, 14, 0), false),
            new Reservation(null, event2, service7, LocalDateTime.of(2027, 6, 13, 14, 0), LocalDateTime.of(2027, 6, 13, 15, 0), false)
        );

        for (Reservation reservation : reservations) {
            reservationService.saveReservation(reservation);
        }
    }

    @Test
    @Transactional
    public void createReservation_ValidInput_ReturnsCreatedReservation() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        event.setDate(LocalDateTime.now().plusDays(20));
        event = eventService.save(event);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusMinutes(service.getMinReservationTime()));

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isCreated()) // 201
            .andExpect(jsonPath("$.selectedEventId").value(newReservationDTO.getSelectedEventId().intValue()))
            .andExpect(jsonPath("$.selectedServiceId").value(newReservationDTO.getSelectedServiceId().intValue()));

        Mockito.verify(emailService, Mockito.times(1)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_NonexistentEvent_ReturnsBadRequest() throws Exception {
        login();

        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(55555L);
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(null);
        newReservationDTO.setReservationEndDateTime(null);

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("selectedEventId: Selected event does not exist."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_NonexistentService_ReturnsBadRequest() throws Exception {
        login();

        Event event = eventService.getEvent(1L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(55555L);
        newReservationDTO.setReservationStartDateTime(null);
        newReservationDTO.setReservationEndDateTime(null);

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("selectedServiceId: Selected service does not exist."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_ServiceUnavailable_ReturnsBadRequest() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(5L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(null);
        newReservationDTO.setReservationEndDateTime(null);

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("selectedServiceId: Selected service is not available."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_StartTimeInPast_ReturnsBadRequest() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        event.setDate(LocalDateTime.now());
        event = eventService.save(event);

        Service service = (Service) solutionService.getSolution(7L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).minusMinutes(1));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusHours(service.getMinReservationTime() / 60).plusMinutes(service.getMinReservationTime() % 60).minusMinutes(1));

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: It's too late to make a reservation."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_EndTimeBeforeStartTime_ReturnsBadRequest() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        event.setDate(LocalDateTime.now().plusDays(20));
        event = eventService.save(event);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusHours(1));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusMinutes(service.getMinReservationTime()));

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationEndDateTime: Reservation end time must be after start time."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_DurationTooShort_ReturnsBadRequest() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        event.setDate(LocalDateTime.now().plusDays(20));
        event = eventService.save(event);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusMinutes(service.getMinReservationTime() - 1));

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: " + "Reservation duration must be between " + service.getMinReservationTime() + " and " + service.getMaxReservationTime() + " minutes."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_DurationTooLong_ReturnsBadRequest() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        event.setDate(LocalDateTime.now().plusDays(20));
        event = eventService.save(event);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusDays(1));

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: " + "Reservation duration must be between " + service.getMinReservationTime() + " and " + service.getMaxReservationTime() + " minutes."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_ReservationOverlapsAfter_ReturnsBadRequest() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(2027, 6,14, 14, 30));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(2027, 6, 14, 15, service.getMinReservationTime()));

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: The selected time overlaps with an existing reservation."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_ReservationOverlapsBefore_ReturnsBadRequest() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(2027, 6,14, 13, 30));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(2027, 6, 14, 14, service.getMinReservationTime()));

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: The selected time overlaps with an existing reservation."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_ReservationOverlapsBeforeAndAfter_ReturnsBadRequest() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(2027, 6,10, 10, 30));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(2027, 6, 10, 11, 30));

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: The selected time overlaps with an existing reservation."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_ReservationOverlapsWithDifferentServiceReservations_ReturnsCreatedReservation() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        event.setDate(LocalDateTime.of(2027, 8, 13, 13, 0));
        eventService.save(event);

        Service service8 = (Service) solutionService.getSolution(8L);

        List<Reservation> reservations = List.of(
                new Reservation(null, event, service8, LocalDateTime.of(2027, 8, 13, 13, 0), LocalDateTime.of(2027, 8, 13, 14, 0), false),
                new Reservation(null, event, service8, LocalDateTime.of(2027, 8, 13, 14, 0), LocalDateTime.of(2027, 8, 13, 15, 0), false)
        );

        for (Reservation reservation : reservations) {
            reservationService.saveReservation(reservation);
        }

        Service service6 = (Service) solutionService.getSolution(6L);

        // would overlap with: (but not since services are different)
        // event1, service8, LocalDateTime.of(2027, 8, 13, 13, 0), LocalDateTime.of(2027, 8, 13, 14, 0)),
        // event1, service8, LocalDateTime.of(2027, 8, 13, 14, 0), LocalDateTime.of(2027, 8, 13, 15, 0))

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service6.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(2027, 8,13, 13, 30));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(2027, 8, 13, 14, 30));

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newReservationDTO)))
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("$.selectedEventId").value(newReservationDTO.getSelectedEventId().intValue()))
                .andExpect(jsonPath("$.selectedServiceId").value(newReservationDTO.getSelectedServiceId().intValue()));

        Mockito.verify(emailService, Mockito.times(1)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    @Test
    @Transactional
    public void createReservation_ReservationNotOnEventDate_ReturnsBadRequest() throws Exception {
        login();

        Event event = eventService.getEvent(1L);
        event.setDate(LocalDateTime.now().plusDays(20));
        event = eventService.save(event);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(),event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusDays(5));
        newReservationDTO.setReservationEndDateTime(newReservationDTO.getReservationStartDateTime().plusHours(1));

        Mockito.doNothing().when(emailService).sendReservationConfirmation(Mockito.any(Reservation.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .header("Authorization", "Bearer " + jwtToken)  // Include token
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: Reservation must be the same day as the event start date."));
        Mockito.verify(emailService, Mockito.times(0)).sendReservationConfirmation(Mockito.any(Reservation.class));

        logout();
    }

    private void login() throws Exception {
        LoginDTO loginDTO = new LoginDTO("tac@gmail.com", "admin");

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
}