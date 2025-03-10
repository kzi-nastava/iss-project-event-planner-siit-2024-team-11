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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = EventyApplication.class) // ✅ Ensures it loads full Spring context
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") // ✅ Uses test database
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
        reservationDTO.setSelectedServiceId(2L);
        reservationDTO.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        reservationDTO.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 11, 0));

        Event mockEvent = new Event();
        mockEvent.setId(1L);

        Solution mockService = new Service();
        mockService.setId(2L);

        Reservation expectedReservation = new Reservation();
        expectedReservation.setSelectedEvent(mockEvent);
        expectedReservation.setSelectedService(mockService);
        expectedReservation.setReservationStartDateTime(reservationDTO.getReservationStartDateTime());
        expectedReservation.setReservationEndDateTime(reservationDTO.getReservationEndDateTime());

        Mockito.when(eventService.getEvent(1L)).thenReturn(mockEvent);
        Mockito.when(serviceService.getService(2L)).thenReturn(mockService);
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(expectedReservation);

        Reservation createdReservation = reservationService.createReservation(reservationDTO);

        assertNotNull(createdReservation);
        assertEquals(mockEvent, createdReservation.getSelectedEvent());
        assertEquals(mockService, createdReservation.getSelectedService());

        verify(reservationRepository, times(1)).save(Mockito.any(Reservation.class));
    }
}