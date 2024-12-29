package org.example.eventy.solutions.repositories;

import org.example.eventy.events.models.Event;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    ArrayList<Reservation> findAllBySelectedService(Solution service);

    ArrayList<Reservation> findAllBySelectedEvent(Event event);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.selectedEvent.organiser.id = :userId ")
    Page<Reservation> findAllByUserId(@Param("userId") Long userId,
                                      Pageable pageable);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.selectedService.provider.id = :providerId " +
            "AND r.reservationStartDateTime >= :startDate " +
            "AND r.reservationEndDateTime <= :endDate")
    List<Reservation> findReservationsByProvider(
            @Param("providerId") Long providerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<Reservation> findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfter(
            LocalDateTime end, LocalDateTime start);
}
