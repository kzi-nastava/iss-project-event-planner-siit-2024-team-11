package service;

import org.example.eventy.EventyApplication;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.dtos.ReservationDTO;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.repositories.ReservationRepository;
import org.example.eventy.solutions.services.ReservationService;
import org.example.eventy.solutions.services.ServiceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = EventyApplication.class) // loads full Spring context
@ActiveProfiles("test") // uses test database
public class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;
    @MockBean
    private ReservationRepository reservationRepository;
    @MockBean
    private EventService eventService;
    @MockBean
    private ServiceService serviceService;

    @Test
    public void createReservation_SuccessfulReservation_True() {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setSelectedEventId(1L);
        reservationDTO.setSelectedServiceId(6L);
        reservationDTO.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        reservationDTO.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 11, 0));

        Event mockEvent = new Event();
        mockEvent.setId(1L);

        Solution mockService = new Service();
        mockService.setId(6L);

        Reservation expectedReservation = new Reservation();
        expectedReservation.setSelectedEvent(mockEvent);
        expectedReservation.setSelectedService(mockService);
        expectedReservation.setReservationStartDateTime(reservationDTO.getReservationStartDateTime());
        expectedReservation.setReservationEndDateTime(reservationDTO.getReservationEndDateTime());

        Mockito.when(eventService.getEvent(1L)).thenReturn(mockEvent);
        Mockito.when(serviceService.getService(6L)).thenReturn(mockService);
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(expectedReservation);

        Reservation createdReservation = reservationService.createReservation(reservationDTO);

        assertNotNull(createdReservation);
        assertEquals(mockEvent, createdReservation.getSelectedEvent());
        assertEquals(mockService, createdReservation.getSelectedService());

        verify(reservationRepository, times(1)).save(Mockito.any(Reservation.class));
    }

    @Test
    public void findOverlappingReservations_ValidOverlap_True() {
        Solution mockService = new Service();
        mockService.setId(6L);

        Reservation existingReservation = new Reservation();
        existingReservation.setSelectedService(mockService);
        existingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 9, 30));
        existingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 30));

        List<Reservation> expectedReservations = new ArrayList<>();
        expectedReservations.add(existingReservation);

        Mockito.when(serviceService.getService(6L)).thenReturn(mockService);
        Mockito.when(reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(expectedReservations);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setSelectedServiceId(6L);
        reservationDTO.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        reservationDTO.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 11, 0));

        List<Reservation> overlappingReservations = reservationService.findOverlappingReservations(reservationDTO);

        assertNotNull(overlappingReservations);
        assertFalse(overlappingReservations.isEmpty());
        assertEquals(1, overlappingReservations.size());
        assertEquals(mockService, overlappingReservations.get(0).getSelectedService());
    }
}