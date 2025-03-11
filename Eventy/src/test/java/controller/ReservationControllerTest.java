package controller;

import org.example.eventy.EventyApplication;
import org.example.eventy.common.services.EmailService;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.ReservationService;
import org.example.eventy.solutions.services.SolutionService;
import org.example.eventy.users.models.EventOrganizer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = EventyApplication.class)
@AutoConfigureMockMvc
public class ReservationControllerTest {
    @MockBean
    private ReservationService reservationService;
    @MockBean
    private EventService eventService;
    @MockBean
    private SolutionService solutionService;
    @MockBean
    private EmailService emailService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getReservation_ReservationExists_True() throws Exception {
        Event mockEvent = new Event();
        mockEvent.setId(5L);

        Solution mockService = new Service();
        mockService.setId(6L);

        Long mockReservationId = 1L;
        Reservation mockReservation = new Reservation();
        mockReservation.setId(mockReservationId);
        mockReservation.setSelectedEvent(mockEvent);
        mockReservation.setSelectedService(mockService);

        Mockito.when(reservationService.getReservation(mockReservationId)).thenReturn(mockReservation);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/{reservationId}", mockReservationId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.selectedEventId").value(mockEvent.getId()))
            .andExpect(jsonPath("$.selectedServiceId").value(mockService.getId()));
    }

    @Test
    public void getReservation_ReservationExists_False() throws Exception {
        Long mockReservationId = 1L;

        Mockito.when(reservationService.getReservation(mockReservationId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/{reservationId}", mockReservationId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound()); // 404
    }

    @Test
    public void getReservationsByServiceId_ReservationsExist_True() throws Exception {
        Event mockEvent = new Event();
        mockEvent.setId(5L);

        Long mockServiceId = 6L;
        Solution mockService = new Service();
        mockService.setId(mockServiceId);

        Reservation mockReservation1 = new Reservation();
        mockReservation1.setId(1L);
        mockReservation1.setSelectedService(mockService);
        mockReservation1.setSelectedEvent(mockEvent);

        Reservation mockReservation2 = new Reservation();
        mockReservation2.setId(2L);
        mockReservation2.setSelectedService(mockService);
        mockReservation2.setSelectedEvent(mockEvent);

        ArrayList<Reservation> expectedReservations = new ArrayList<>();
        expectedReservations.add(mockReservation1);
        expectedReservations.add(mockReservation2);

        Mockito.when(reservationService.getReservationsByServiceId(mockServiceId)).thenReturn(expectedReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/service/{serviceId}", mockServiceId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.size()", Matchers.is(2)))
            .andExpect(jsonPath("$[0].selectedServiceId", Matchers.is(mockServiceId.intValue())))
            .andExpect(jsonPath("$[1].selectedServiceId", Matchers.is(mockServiceId.intValue())));
    }

    @Test
    public void getReservationsByServiceId_ReservationsEmpty_True() throws Exception {
        Long mockServiceId = 6L;

        ArrayList<Reservation> expectedReservations = new ArrayList<>();

        Mockito.when(reservationService.getReservationsByServiceId(mockServiceId)).thenReturn(expectedReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/service/{serviceId}", mockServiceId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void getReservationsByServiceId_ReservationsExist_False() throws Exception {
        Long mockServiceId = 6L;

        Mockito.when(reservationService.getReservationsByServiceId(mockServiceId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/service/{serviceId}", mockServiceId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound()); // 404
    }

    @Test
    public void getReservationsByEventId_ReservationsExist_True() throws Exception {
        Long mockEventId = 5L;
        Event mockEvent = new Event();
        mockEvent.setId(mockEventId);

        Solution mockService = new Service();
        mockService.setId(6L);

        Reservation mockReservation1 = new Reservation();
        mockReservation1.setId(1L);
        mockReservation1.setSelectedService(mockService);
        mockReservation1.setSelectedEvent(mockEvent);

        Reservation mockReservation2 = new Reservation();
        mockReservation2.setId(2L);
        mockReservation2.setSelectedService(mockService);
        mockReservation2.setSelectedEvent(mockEvent);

        ArrayList<Reservation> expectedReservations = new ArrayList<>();
        expectedReservations.add(mockReservation1);
        expectedReservations.add(mockReservation2);

        Mockito.when(reservationService.getReservationsByEventId(mockEventId)).thenReturn(expectedReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/event/{eventId}", mockEventId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.size()", Matchers.is(2)))
            .andExpect(jsonPath("$[0].selectedEventId", Matchers.is(mockEventId.intValue())))
            .andExpect(jsonPath("$[1].selectedEventId", Matchers.is(mockEventId.intValue())));
    }

    @Test
    public void getReservationsByEventId_ReservationsEmpty_True() throws Exception {
        Long mockEventId = 5L;

        ArrayList<Reservation> expectedReservations = new ArrayList<>();

        Mockito.when(reservationService.getReservationsByEventId(mockEventId)).thenReturn(expectedReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/event/{eventId}", mockEventId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void getReservationsByEventId_ReservationsExist_False() throws Exception {
        Long mockEventId = 5L;

        Mockito.when(reservationService.getReservationsByEventId(mockEventId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/event/{eventId}", mockEventId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound()); // 404
    }

    @Test
    public void getReservationsByUserId_ReservationsExist_True() throws Exception {
        Long mockUserId = 1L;
        EventOrganizer mockEventOrganizer = new EventOrganizer();
        mockEventOrganizer.setId(mockUserId);

        Event mockEvent = new Event();
        mockEvent.setOrganiser(mockEventOrganizer);
        mockEvent.setId(5L);

        Solution mockService = new Service();
        mockService.setId(6L);

        Reservation mockReservation1 = new Reservation();
        mockReservation1.setId(1L);
        mockReservation1.setSelectedService(mockService);
        mockReservation1.setSelectedEvent(mockEvent);

        Reservation mockReservation2 = new Reservation();
        mockReservation2.setId(2L);
        mockReservation2.setSelectedService(mockService);
        mockReservation2.setSelectedEvent(mockEvent);

        ArrayList<Reservation> reservationList = new ArrayList<>();
        reservationList.add(mockReservation1);
        reservationList.add(mockReservation2);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Reservation> expectedReservations = new PageImpl<>(reservationList, pageable, reservationList.size());

        Mockito.when(reservationService.getReservationsByUserId(mockUserId, pageable)).thenReturn(expectedReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/user/{userId}", mockUserId)
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.content.size()", Matchers.is(2)))
            .andExpect(jsonPath("$.content[0].selectedServiceId", Matchers.is(6)))
            .andExpect(jsonPath("$.content[0].selectedEventId", Matchers.is(5)))
            .andExpect(jsonPath("$.content[1].selectedServiceId", Matchers.is(6)))
            .andExpect(jsonPath("$.content[1].selectedEventId", Matchers.is(5)));
    }

    @Test
    public void getReservationsByUserId_ReservationsEmpty_True() throws Exception {
        Long mockUserId = 1L;

        ArrayList<Reservation> reservationList = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 5);
        Page<Reservation> expectedReservations = new PageImpl<>(reservationList, pageable, 0);

        Mockito.when(reservationService.getReservationsByUserId(mockUserId, pageable)).thenReturn(expectedReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/user/{userId}", mockUserId)
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$.content.size()", Matchers.is(0)));
    }

    @Test
    public void getReservationsByUserId_ReservationsExist_False() throws Exception {
        Long mockUserId = 1L;

        Pageable pageable = PageRequest.of(0, 5);
        Mockito.when(reservationService.getReservationsByUserId(mockUserId, pageable)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/user/{userId}", mockUserId)
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound()); // 404
    }

    @Test
    public void createReservation_ReservationsCreated_True() throws Exception {
        Long mockUserId = 1L;
        EventOrganizer mockEventOrganizer = new EventOrganizer();
        mockEventOrganizer.setId(mockUserId);

        Event mockEvent = new Event();
        mockEvent.setOrganiser(mockEventOrganizer);
        mockEvent.setId(5L);

        Solution mockService = new Service();
        mockService.setId(6L);

        Reservation mockReservation1 = new Reservation();
        mockReservation1.setId(1L);
        mockReservation1.setSelectedService(mockService);
        mockReservation1.setSelectedEvent(mockEvent);

        Reservation mockReservation2 = new Reservation();
        mockReservation2.setId(2L);
        mockReservation2.setSelectedService(mockService);
        mockReservation2.setSelectedEvent(mockEvent);

        ArrayList<Reservation> reservationList = new ArrayList<>();
        reservationList.add(mockReservation1);
        reservationList.add(mockReservation2);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Reservation> expectedReservations = new PageImpl<>(reservationList, pageable, reservationList.size());

        Mockito.when(reservationService.getReservationsByUserId(mockUserId, pageable)).thenReturn(expectedReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/user/{userId}", mockUserId)
                        .param("page", "0")
                        .param("size", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.content.size()", Matchers.is(2)))
                .andExpect(jsonPath("$.content[0].selectedServiceId", Matchers.is(6)))
                .andExpect(jsonPath("$.content[0].selectedEventId", Matchers.is(5)))
                .andExpect(jsonPath("$.content[1].selectedServiceId", Matchers.is(6)))
                .andExpect(jsonPath("$.content[1].selectedEventId", Matchers.is(5)));
    }
}
