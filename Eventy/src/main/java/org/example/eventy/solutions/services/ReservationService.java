package org.example.eventy.solutions.services;

import org.example.eventy.solutions.dtos.ReservationDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;

@Service
public class ReservationService {
    // @Autowired
    // private reservationRepository reservationRepository;

    public ReservationDTO createReservation(ReservationDTO reservation) {
        ReservationDTO newReservation = new ReservationDTO();

        newReservation.setId(reservation.getId());
        newReservation.setSelectedEventId(reservation.getSelectedEventId());
        newReservation.setSelectedServiceId(reservation.getSelectedServiceId());
        newReservation.setReservationStartDateTime(reservation.getReservationStartDateTime());
        newReservation.setReservationEndDateTime(reservation.getReservationEndDateTime());

        return saveReservation(newReservation);
    }

    public ReservationDTO getReservation(Long reservationId) {
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(2024, Calendar.JUNE, 15, 14, 0);
        Calendar endDateTime = Calendar.getInstance();
        endDateTime.set(2024, Calendar.JUNE, 15, 17, 0);

        ReservationDTO reservation = new ReservationDTO(
                reservationId, 3L, 2L, startDateTime, endDateTime
        );

        return reservation;
    }

    public ArrayList<ReservationDTO> getReservationsByServiceId(Long serviceId) {
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(2024, Calendar.JUNE, 15, 14, 30);
        Calendar endDateTime = Calendar.getInstance();
        endDateTime.set(2024, Calendar.JUNE, 15, 17, 15);

        ReservationDTO reservation1 = new ReservationDTO(
                1L, 2L, serviceId, startDateTime, endDateTime
        );

        startDateTime.set(2025, Calendar.JANUARY, 10, 8, 0);
        endDateTime.set(2025, Calendar.JANUARY, 10, 11, 30);

        ReservationDTO reservation2 = new ReservationDTO(
                2L, 3L, serviceId, startDateTime, endDateTime
        );

        ArrayList<ReservationDTO> reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);

        return reservations;
    }

    public ArrayList<ReservationDTO> getReservationsByEventId(Long eventId) {
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(2026, Calendar.DECEMBER, 24, 19, 0);
        Calendar endDateTime = Calendar.getInstance();
        endDateTime.set(2026, Calendar.DECEMBER, 24, 20, 30);

        ReservationDTO reservation1 = new ReservationDTO(
                1L, eventId, 2L, startDateTime, endDateTime
        );

        startDateTime.set(2027, Calendar.MARCH, 1, 9, 0);
        endDateTime.set(2027, Calendar.MARCH, 1, 12, 0);

        ReservationDTO reservation2 = new ReservationDTO(
                2L, eventId, 3L, startDateTime, endDateTime
        );

        ArrayList<ReservationDTO> reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);

        return reservations;
    }

    public ReservationDTO saveReservation(ReservationDTO reservation) {
        return reservation;
    }
}
