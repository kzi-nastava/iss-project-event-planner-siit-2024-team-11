package org.example.eventy.solutions.repositories;

import org.example.eventy.solutions.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.selectedService.provider.id = :providerId " +
            "AND r.reservationStartDateTime >= :startDate " +
            "AND r.reservationEndDateTime <= :endDate")
    List<Reservation> findReservationsByProvider(
            @Param("providerId") Long providerId,
            @Param("startDate") Calendar startDate,
            @Param("endDate") Calendar endDate);
}
