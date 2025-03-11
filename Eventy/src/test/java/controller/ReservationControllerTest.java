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
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
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
@Transactional
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

    @Test
    public void getReservation_ReservationExists_ReturnsReservation() throws Exception {
        Long reservationId = 1L;
        Reservation expectedReservation = reservationService.getReservation(reservationId);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/{reservationId}", reservationId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.selectedEventId").value(expectedReservation.getSelectedEvent().getId()))
            .andExpect(jsonPath("$.selectedServiceId").value(expectedReservation.getSelectedService().getId()));
    }

    @Test
    public void getReservation_NoReservationExists_ReturnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/{reservationId}", 55555L)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound()); // 404
    }

    @Test
    public void getReservationsByServiceId_ReservationsExist_ReturnsReservations() throws Exception {
        long serviceId = 6L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/service/{serviceId}", serviceId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.size()", Matchers.is(5)))
            .andExpect(jsonPath("$[0].selectedServiceId", Matchers.is((int) serviceId)))
            .andExpect(jsonPath("$[1].selectedServiceId", Matchers.is((int) serviceId)))
            .andExpect(jsonPath("$[2].selectedServiceId", Matchers.is((int) serviceId)))
            .andExpect(jsonPath("$[3].selectedServiceId", Matchers.is((int) serviceId)))
            .andExpect(jsonPath("$[4].selectedServiceId", Matchers.is((int) serviceId)));

        serviceId = 7L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/service/{serviceId}", serviceId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].selectedServiceId", Matchers.is((int) serviceId)))
                .andExpect(jsonPath("$[1].selectedServiceId", Matchers.is((int) serviceId)));
    }

    @Test
    public void getReservationsByServiceId_NoReservationsExists_ReturnsEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/service/{serviceId}", 55555L)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void getReservationsByEventId_ReservationsExist_ReturnsReservations() throws Exception {
        long eventId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/event/{eventId}", eventId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.size()", Matchers.is(5)))
            .andExpect(jsonPath("$[0].selectedEventId", Matchers.is((int) eventId)))
            .andExpect(jsonPath("$[1].selectedEventId", Matchers.is((int) eventId)))
            .andExpect(jsonPath("$[2].selectedEventId", Matchers.is((int) eventId)))
            .andExpect(jsonPath("$[3].selectedEventId", Matchers.is((int) eventId)))
            .andExpect(jsonPath("$[4].selectedEventId", Matchers.is((int) eventId)));

        eventId = 2L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/event/{eventId}", eventId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.size()", Matchers.is(2)))
            .andExpect(jsonPath("$[0].selectedEventId", Matchers.is((int) eventId)))
            .andExpect(jsonPath("$[1].selectedEventId", Matchers.is((int) eventId)));
    }

    @Test
    public void getReservationsByEventId_NoReservationsExists_ReturnsEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/event/{eventId}", 55555L)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void getReservationsByUserId_ReservationsExist_ReturnsReservations() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Reservation> expectedReservations = reservationService.getReservationsByUserId(1L, pageable);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/user/{userId}", 1L)
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.content.size()", Matchers.is(5)))
            .andExpect(jsonPath("$.totalElements", Matchers.is(7)))
            .andExpect(jsonPath("$.content[0].selectedServiceId", Matchers.is(expectedReservations.getContent().get(0).getSelectedService().getId().intValue())))
            .andExpect(jsonPath("$.content[1].selectedServiceId", Matchers.is(expectedReservations.getContent().get(1).getSelectedService().getId().intValue())))
            .andExpect(jsonPath("$.content[2].selectedServiceId", Matchers.is(expectedReservations.getContent().get(2).getSelectedService().getId().intValue())))
            .andExpect(jsonPath("$.content[3].selectedServiceId", Matchers.is(expectedReservations.getContent().get(3).getSelectedService().getId().intValue())))
            .andExpect(jsonPath("$.content[4].selectedServiceId", Matchers.is(expectedReservations.getContent().get(4).getSelectedService().getId().intValue())));
    }

    @Test
    public void getReservationsByUserId_NoReservationsExists_ReturnsEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/user/{userId}", 5L)
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.content.size()", Matchers.is(0)));
    }

    @Test
    public void createReservation_ValidInput_ReturnsCreatedReservation() throws Exception {
        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusMinutes(service.getMinReservationTime()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isCreated()) // 201
            .andExpect(jsonPath("$.selectedEventId").value(newReservationDTO.getSelectedEventId().intValue()))
            .andExpect(jsonPath("$.selectedServiceId").value(newReservationDTO.getSelectedServiceId().intValue()));
    }

    @Test
    public void createReservation_NonexistentEvent_ReturnsBadRequest() throws Exception {
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(55555L);
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(null);
        newReservationDTO.setReservationEndDateTime(null);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("selectedEventId: Selected event does not exist."));
    }

    @Test
    public void createReservation_NonexistentService_ReturnsBadRequest() throws Exception {
        Event event = eventService.getEvent(1L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(55555L);
        newReservationDTO.setReservationStartDateTime(null);
        newReservationDTO.setReservationEndDateTime(null);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("selectedServiceId: Selected service does not exist."));
    }

    @Test
    public void createReservation_ServiceUnavailable_ReturnsBadRequest() throws Exception {
        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(5L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(null);
        newReservationDTO.setReservationEndDateTime(null);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("selectedServiceId: Selected service is not available."));
    }

    @Test
    public void createReservation_StartTimeInPast_ReturnsBadRequest() throws Exception {
        Event event = eventService.getEvent(1L);
        event.setDate(LocalDateTime.now());
        eventService.save(event);

        event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(7L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusHours(service.getMinReservationTime() / 60).plusMinutes(service.getMinReservationTime() % 60));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: It's too late to make a reservation."));
    }

    @Test
    public void createReservation_EndTimeBeforeStartTime_ReturnsBadRequest() throws Exception {
        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusHours(1));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusMinutes(service.getMinReservationTime()));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationEndDateTime: Reservation end time must be after start time."));
    }

    @Test
    public void createReservation_DurationTooShort_ReturnsBadRequest() throws Exception {
        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusMinutes(service.getMinReservationTime() - 1));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: " + "Reservation duration must be between " + service.getMinReservationTime() + " and " + service.getMaxReservationTime() + " minutes."));
    }

    @Test
    public void createReservation_DurationTooLong_ReturnsBadRequest() throws Exception {
        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusDays(1));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: " + "Reservation duration must be between " + service.getMinReservationTime() + " and " + service.getMaxReservationTime() + " minutes."));
    }

    @Test
    public void createReservation_ReservationOverlapsAfter_ReturnsBadRequest() throws Exception {
        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(2027, 6,14, 14, 30));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(2027, 6, 14, 15, service.getMinReservationTime()));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: The selected time overlaps with an existing reservation."));
    }

    @Test
    public void createReservation_ReservationOverlapsBefore_ReturnsBadRequest() throws Exception {
        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(2027, 6,14, 13, 30));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(2027, 6, 14, 14, service.getMinReservationTime()));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newReservationDTO)))
                .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: The selected time overlaps with an existing reservation."));
    }

    @Test
    public void createReservation_ReservationOverlapsBeforeAndAfter_ReturnsBadRequest() throws Exception {
        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(2027, 6,10, 10, 30));
        newReservationDTO.setReservationEndDateTime(LocalDateTime.of(2027, 6, 10, 11, 30));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newReservationDTO)))
                .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: The selected time overlaps with an existing reservation."));
    }

    @Test
    public void createReservation_ReservationNotOnEventDate_ReturnsBadRequest() throws Exception {
        Event event = eventService.getEvent(1L);
        Service service = (Service) solutionService.getSolution(6L);

        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setSelectedEventId(event.getId());
        newReservationDTO.setSelectedServiceId(service.getId());
        newReservationDTO.setReservationStartDateTime(LocalDateTime.of(event.getDate().getYear(), event.getDate().getMonth(),event.getDate().getDayOfMonth(), event.getDate().getHour(), 0).plusDays(5));
        newReservationDTO.setReservationEndDateTime(newReservationDTO.getReservationStartDateTime().plusHours(1));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations", newReservationDTO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newReservationDTO)))
            .andExpect(status().isBadRequest()); // 400

        String responseBody = result.andReturn().getResponse().getContentAsString();
        List<String> errorMessages = objectMapper.readValue(responseBody, List.class);

        assertTrue(errorMessages.contains("reservationStartDateTime: Reservation must be the same day as the event start date."));
    }
}