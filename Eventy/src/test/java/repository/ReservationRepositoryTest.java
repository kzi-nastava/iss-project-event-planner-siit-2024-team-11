package repository;

import org.example.eventy.EventyApplication;
import org.example.eventy.events.repositories.EventRepository;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.repositories.ReservationRepository;
import org.example.eventy.solutions.repositories.SolutionRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = EventyApplication.class) // ✅ Ensures it loads full Spring context
@ActiveProfiles("test") // ✅ Uses test database
@Transactional // ✅ Ensures DB is cleaned up after each test
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private EventRepository eventRepository;

    @Test
    @Order(1)
    public void findAllBySelectedService_ReservationsExist_True() {
        Solution randomSolution = solutionRepository.findById(6L).get();

        Reservation reservation1 = new Reservation();
        reservation1.setSelectedService(randomSolution);
        reservation1.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation1.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation1.setSelectedEvent(eventRepository.findById(1L).get());
        reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setSelectedService(randomSolution);
        reservation2.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation2.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation2.setSelectedEvent(eventRepository.findById(2L).get());
        reservationRepository.save(reservation2);

        ArrayList<Reservation> reservationsByService = reservationRepository.findAllBySelectedService(randomSolution);
        for (Reservation reservation : reservationsByService) {
            assertEquals(reservation.getSelectedService().getId(), randomSolution.getId());
        }

        assertEquals(2, reservationsByService.size());
    }

    @Test
    @Order(2)
    public void findAllBySelectedService_ReservationsExist_False() {
        Solution randomSolution = solutionRepository.findById(7L).get();
        ArrayList<Reservation> reservationsByService = reservationRepository.findAllBySelectedService(randomSolution);

        assertEquals(0, reservationsByService.size());
    }
}
