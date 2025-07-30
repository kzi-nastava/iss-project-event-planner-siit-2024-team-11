package repository;

import org.example.eventy.EventyApplication;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.repositories.EventRepository;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.repositories.ReservationRepository;
import org.example.eventy.solutions.repositories.SolutionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EventyApplication.class) // loads full Spring context
@ActiveProfiles("test") // uses test database
@Transactional // DB is cleaned up after each test
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void findAllBySelectedService_ReservationsExist_ReturnsReservations() {
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

        // make a random reservation for a service that is not being tested
        Reservation reservation3 = new Reservation();
        reservation3.setSelectedService(solutionRepository.findById(7L).get());
        reservation3.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation3.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation3.setSelectedEvent(eventRepository.findById(3L).get());
        reservationRepository.save(reservation3);

        ArrayList<Reservation> reservationsByService = reservationRepository.findAllBySelectedService(randomSolution);
        for (Reservation reservation : reservationsByService) {
            assertEquals(reservation.getSelectedService().getId(), randomSolution.getId());
        }

        assertEquals(2, reservationsByService.size());
    }

    @Test
    public void findAllBySelectedService_NoReservationsExist_ReturnsEmptyList() {
        Solution randomSolution = solutionRepository.findById(7L).get();

        // make a random reservation for a service that is not being tested
        Reservation reservation = new Reservation();
        reservation.setSelectedService(solutionRepository.findById(6L).get());
        reservation.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation.setSelectedEvent(eventRepository.findById(3L).get());
        reservationRepository.save(reservation);

        ArrayList<Reservation> reservationsByService = reservationRepository.findAllBySelectedService(randomSolution);

        assertEquals(0, reservationsByService.size());
    }

    @Test
    public void findAllBySelectedEvent_ReservationsExist_ReturnsReservations() {
        Event randomEvent = eventRepository.findById(1L).get();

        Reservation reservation1 = new Reservation();
        reservation1.setSelectedService(solutionRepository.findById(6L).get());
        reservation1.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation1.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation1.setSelectedEvent(randomEvent);
        reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setSelectedService(solutionRepository.findById(7L).get());
        reservation2.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation2.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation2.setSelectedEvent(randomEvent);
        reservationRepository.save(reservation2);

        // make a random reservation for an event that is not being tested
        Reservation reservation3 = new Reservation();
        reservation3.setSelectedService(solutionRepository.findById(7L).get());
        reservation3.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation3.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation3.setSelectedEvent(eventRepository.findById(3L).get());
        reservationRepository.save(reservation3);

        ArrayList<Reservation> reservationsByEvent = reservationRepository.findAllBySelectedEvent(randomEvent);
        for (Reservation reservation : reservationsByEvent) {
            assertEquals(reservation.getSelectedEvent().getId(), randomEvent.getId());
        }

        assertEquals(2, reservationsByEvent.size());
    }

    @Test
    public void findAllBySelectedEvent_NoReservationsExist_ReturnsEmptyList() {
        Event randomEvent = eventRepository.findById(1L).get();
        ArrayList<Reservation> reservationsByService = reservationRepository.findAllBySelectedEvent(randomEvent);

        // make a random reservation for an event that is not being tested
        Reservation reservation = new Reservation();
        reservation.setSelectedService(solutionRepository.findById(6L).get());
        reservation.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation.setSelectedEvent(eventRepository.findById(3L).get());
        reservationRepository.save(reservation);

        assertEquals(0, reservationsByService.size());
    }

    @Test
    public void findAllByUserId_ReservationsExist_ReturnsReservations() {
        Pageable pageable = PageRequest.of(0, 5);

        Reservation reservation1 = new Reservation();
        reservation1.setSelectedService(solutionRepository.findById(6L).get());
        reservation1.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation1.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation1.setSelectedEvent(eventRepository.findById(1L).get()); // tac@gmail.com, id=1
        reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setSelectedService(solutionRepository.findById(7L).get());
        reservation2.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation2.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation2.setSelectedEvent(eventRepository.findById(1L).get()); // tac@gmail.com, id=1
        reservationRepository.save(reservation2);

        // make a random reservation by another user
        Reservation reservation3 = new Reservation();
        reservation3.setSelectedService(solutionRepository.findById(7L).get());
        reservation3.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation3.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation3.setSelectedEvent(eventRepository.findById(3L).get()); // ves@gmail.com, id=2
        reservationRepository.save(reservation3);

        Page<Reservation> reservationsByUserWithId1 = reservationRepository.findAllByUserId(1L, pageable);
        assertEquals(2, reservationsByUserWithId1.getTotalElements());

        Page<Reservation> reservationsByUserWithId2 = reservationRepository.findAllByUserId(2L, pageable);
        assertEquals(1, reservationsByUserWithId2.getTotalElements());
    }

    @Test
    public void findAllByUserId_NoReservationsExist_ReturnsEmptyList() {
        Pageable pageable = PageRequest.of(0, 5);

        // create a random reservation by another user
        Reservation reservation = new Reservation();
        reservation.setSelectedService(solutionRepository.findById(7L).get());
        reservation.setReservationStartDateTime(LocalDateTime.now().plusDays(3));
        reservation.setReservationEndDateTime(LocalDateTime.now().plusMonths(1));
        reservation.setSelectedEvent(eventRepository.findById(3L).get()); // ves@gmail.com, id=2
        reservationRepository.save(reservation);

        Page<Reservation> reservationsByUserWithId1 = reservationRepository.findAllByUserId(1L, pageable);
        assertEquals(0, reservationsByUserWithId1.getNumberOfElements());
    }

    @Test
    public void findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService_ReservationOverlapsAfter_ReturnsOverlappingReservations() {
        Solution randomSolution = solutionRepository.findById(6L).get();

        Reservation existingReservation = new Reservation();
        existingReservation.setSelectedService(randomSolution);
        existingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        existingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 16, 10, 0));
        existingReservation.setSelectedEvent(eventRepository.findById(1L).get());
        reservationRepository.save(existingReservation);

        Reservation overlappingReservation = new Reservation();
        overlappingReservation.setSelectedService(randomSolution);
        overlappingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 14, 12, 0));
        overlappingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 18, 12, 0));
        overlappingReservation.setSelectedEvent(eventRepository.findById(2L).get());

        ArrayList<Reservation> isOverlapping = (ArrayList<Reservation>) reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(
                overlappingReservation.getReservationEndDateTime(),
                overlappingReservation.getReservationStartDateTime(),
                overlappingReservation.getSelectedService()
        );

        assertEquals(1, isOverlapping.size());
    }

    @Test
    public void findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService_NoReservationOverlapsAfter_ReturnsEmptyList() {
        Solution randomSolution = solutionRepository.findById(6L).get();

        Reservation existingReservation = new Reservation();
        existingReservation.setSelectedService(randomSolution);
        existingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        existingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 16, 10, 0));
        existingReservation.setSelectedEvent(eventRepository.findById(1L).get());
        reservationRepository.save(existingReservation);

        Reservation overlappingReservation = new Reservation();
        overlappingReservation.setSelectedService(randomSolution);
        overlappingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 16, 12, 0));
        overlappingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 18, 12, 0));
        overlappingReservation.setSelectedEvent(eventRepository.findById(2L).get());

        ArrayList<Reservation> isOverlapping = (ArrayList<Reservation>) reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(
                overlappingReservation.getReservationEndDateTime(),
                overlappingReservation.getReservationStartDateTime(),
                overlappingReservation.getSelectedService()
        );

        assertEquals(0, isOverlapping.size());
    }

    @Test
    public void findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService_ReservationOverlapsBefore_ReturnsOverlappingReservations() {
        Solution randomSolution = solutionRepository.findById(6L).get();

        Reservation existingReservation = new Reservation();
        existingReservation.setSelectedService(randomSolution);
        existingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        existingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 16, 10, 0));
        existingReservation.setSelectedEvent(eventRepository.findById(1L).get());
        reservationRepository.save(existingReservation);

        Reservation overlappingReservation = new Reservation();
        overlappingReservation.setSelectedService(randomSolution);
        overlappingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 8, 12, 0));
        overlappingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 13, 12, 0));
        overlappingReservation.setSelectedEvent(eventRepository.findById(2L).get());

        ArrayList<Reservation> isOverlapping = (ArrayList<Reservation>) reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(
                overlappingReservation.getReservationEndDateTime(),
                overlappingReservation.getReservationStartDateTime(),
                overlappingReservation.getSelectedService()
        );

        assertEquals(1, isOverlapping.size());
    }

    @Test
    public void findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService_NoReservationOverlapsBefore_ReturnsEmptyList() {
        Solution randomSolution = solutionRepository.findById(6L).get();

        Reservation existingReservation = new Reservation();
        existingReservation.setSelectedService(randomSolution);
        existingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        existingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 16, 10, 0));
        existingReservation.setSelectedEvent(eventRepository.findById(1L).get());
        reservationRepository.save(existingReservation);

        Reservation overlappingReservation = new Reservation();
        overlappingReservation.setSelectedService(randomSolution);
        overlappingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 8, 9, 0));
        overlappingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 9, 0));
        overlappingReservation.setSelectedEvent(eventRepository.findById(2L).get());

        ArrayList<Reservation> isOverlapping = (ArrayList<Reservation>) reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(
                overlappingReservation.getReservationEndDateTime(),
                overlappingReservation.getReservationStartDateTime(),
                overlappingReservation.getSelectedService()
        );

        assertEquals(0, isOverlapping.size());
    }

    @Test
    public void findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService_ReservationOverlapsAfterAndBefore_ReturnsOverlappingReservations() {
        Solution randomSolution = solutionRepository.findById(6L).get();

        Reservation existingReservation1 = new Reservation();
        existingReservation1.setSelectedService(randomSolution);
        existingReservation1.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 6, 10, 0));
        existingReservation1.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        existingReservation1.setSelectedEvent(eventRepository.findById(1L).get());
        reservationRepository.save(existingReservation1);

        Reservation existingReservation2 = new Reservation();
        existingReservation2.setSelectedService(randomSolution);
        existingReservation2.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 15, 12, 0));
        existingReservation2.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 22, 12, 0));
        existingReservation2.setSelectedEvent(eventRepository.findById(2L).get());
        reservationRepository.save(existingReservation2);

        Reservation overlappingReservation = new Reservation();
        overlappingReservation.setSelectedService(randomSolution);
        overlappingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 8, 12, 0));
        overlappingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 18, 12, 0));
        overlappingReservation.setSelectedEvent(eventRepository.findById(2L).get());

        ArrayList<Reservation> isOverlapping = (ArrayList<Reservation>) reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(
                overlappingReservation.getReservationEndDateTime(),
                overlappingReservation.getReservationStartDateTime(),
                overlappingReservation.getSelectedService()
        );

        assertEquals(2, isOverlapping.size());
    }

    @Test
    public void findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService_NoReservationOverlapsAfterAndBefore_ReturnsEmptyList() {
        Solution randomSolution = solutionRepository.findById(6L).get();

        Reservation existingReservation1 = new Reservation();
        existingReservation1.setSelectedService(randomSolution);
        existingReservation1.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 6, 10, 0));
        existingReservation1.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        existingReservation1.setSelectedEvent(eventRepository.findById(1L).get());
        reservationRepository.save(existingReservation1);

        Reservation existingReservation2 = new Reservation();
        existingReservation2.setSelectedService(randomSolution);
        existingReservation2.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 15, 12, 0));
        existingReservation2.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 22, 12, 0));
        existingReservation2.setSelectedEvent(eventRepository.findById(2L).get());
        reservationRepository.save(existingReservation2);

        Reservation overlappingReservation = new Reservation();
        overlappingReservation.setSelectedService(randomSolution);
        overlappingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 12, 0));
        overlappingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 15, 11, 59));
        overlappingReservation.setSelectedEvent(eventRepository.findById(2L).get());

        ArrayList<Reservation> isOverlapping = (ArrayList<Reservation>) reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(
                overlappingReservation.getReservationEndDateTime(),
                overlappingReservation.getReservationStartDateTime(),
                overlappingReservation.getSelectedService()
        );

        assertEquals(0, isOverlapping.size());
    }

    @Test
    public void findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService_ReservationsOverlapsAfterWithAnotherService_ReturnsEmptyList() {
        Reservation existingReservation = new Reservation();
        existingReservation.setSelectedService(solutionRepository.findById(6L).get());
        existingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        existingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 16, 10, 0));
        existingReservation.setSelectedEvent(eventRepository.findById(1L).get());
        reservationRepository.save(existingReservation);

        Reservation otherServiceReservation = new Reservation();
        otherServiceReservation.setSelectedService(solutionRepository.findById(7L).get());
        otherServiceReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 14, 12, 0));
        otherServiceReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 18, 12, 0));
        otherServiceReservation.setSelectedEvent(eventRepository.findById(2L).get());

        ArrayList<Reservation> isOverlapping = (ArrayList<Reservation>) reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(
                otherServiceReservation.getReservationEndDateTime(),
                otherServiceReservation.getReservationStartDateTime(),
                otherServiceReservation.getSelectedService()
        );

        assertEquals(0, isOverlapping.size());
    }

    @Test
    public void findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService_ReservationsOverlapsBeforeWithAnotherService_ReturnsEmptyList() {
        Reservation existingReservation = new Reservation();
        existingReservation.setSelectedService(solutionRepository.findById(6L).get());
        existingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        existingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 16, 10, 0));
        existingReservation.setSelectedEvent(eventRepository.findById(1L).get());
        reservationRepository.save(existingReservation);

        Reservation otherServiceReservation = new Reservation();
        otherServiceReservation.setSelectedService(solutionRepository.findById(7L).get());
        otherServiceReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 8, 12, 0));
        otherServiceReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 13, 12, 0));
        otherServiceReservation.setSelectedEvent(eventRepository.findById(2L).get());

        ArrayList<Reservation> isOverlapping = (ArrayList<Reservation>) reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(
                otherServiceReservation.getReservationEndDateTime(),
                otherServiceReservation.getReservationStartDateTime(),
                otherServiceReservation.getSelectedService()
        );

        assertEquals(0, isOverlapping.size());
    }

    @Test
    public void findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService_ReservationsOverlapsAfterAndBeforeWithAnotherService_ReturnsEmptyList() {
        Reservation existingReservation1 = new Reservation();
        existingReservation1.setSelectedService(solutionRepository.findById(6L).get());
        existingReservation1.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 6, 10, 0));
        existingReservation1.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 10, 10, 0));
        existingReservation1.setSelectedEvent(eventRepository.findById(1L).get());
        reservationRepository.save(existingReservation1);

        Reservation existingReservation2 = new Reservation();
        existingReservation2.setSelectedService(solutionRepository.findById(6L).get());
        existingReservation2.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 15, 12, 0));
        existingReservation2.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 22, 12, 0));
        existingReservation2.setSelectedEvent(eventRepository.findById(2L).get());
        reservationRepository.save(existingReservation2);

        Reservation overlappingReservation = new Reservation();
        overlappingReservation.setSelectedService(solutionRepository.findById(7L).get());
        overlappingReservation.setReservationStartDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 8, 12, 0));
        overlappingReservation.setReservationEndDateTime(LocalDateTime.of(2025, LocalDateTime.now().getMonth().plus(1), 18, 12, 0));
        overlappingReservation.setSelectedEvent(eventRepository.findById(2L).get());

        ArrayList<Reservation> isOverlapping = (ArrayList<Reservation>) reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(
                overlappingReservation.getReservationEndDateTime(),
                overlappingReservation.getReservationStartDateTime(),
                overlappingReservation.getSelectedService()
        );

        assertEquals(0, isOverlapping.size());
    }
}
