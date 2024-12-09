package org.example.eventy.solutions.services;

import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.dtos.ReservationDTO;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private ServiceService serviceService;

    public Reservation createReservation(ReservationDTO reservation) {
        Reservation newReservation = new Reservation();
        newReservation.setId(reservation.getId());
        newReservation.setSelectedEvent(eventService.getEvent(reservation.getSelectedEventId()));
        newReservation.setSelectedService(serviceService.getService(reservation.getSelectedServiceId()));
        newReservation.setReservationStartDateTime(reservation.getReservationStartDateTime());
        newReservation.setReservationEndDateTime(reservation.getReservationEndDateTime());

        return saveReservation(newReservation);
    }

    public Reservation getReservation(Long reservationId) {
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(2024, Calendar.JUNE, 15, 14, 0);
        Calendar endDateTime = Calendar.getInstance();
        endDateTime.set(2024, Calendar.JUNE, 15, 17, 0);

        Long serviceId = 2L;
        Reservation reservation = new Reservation(
                reservationId, eventService.getEvent(1L), serviceService.getService(serviceId), startDateTime, endDateTime
        );

        return reservation;
    }

    public ArrayList<Reservation> getReservationsByServiceId(Long serviceId) {
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(2024, Calendar.JUNE, 15, 14, 30);
        Calendar endDateTime = Calendar.getInstance();
        endDateTime.set(2024, Calendar.JUNE, 15, 17, 15);

        Reservation reservation1 = new Reservation(
            1L, eventService.getEvent(2L), serviceService.getService(serviceId), startDateTime, endDateTime
        );

        startDateTime.set(2025, Calendar.JANUARY, 10, 8, 0);
        endDateTime.set(2025, Calendar.JANUARY, 10, 11, 30);

        Reservation reservation2 = new Reservation(
            2L, eventService.getEvent(3L), serviceService.getService(serviceId), startDateTime, endDateTime
        );

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);

        return reservations;
    }

    public ArrayList<Reservation> getReservationsByEventId(Long eventId) {
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(2027, Calendar.MAY, 1, 21, 30);
        Calendar endDateTime = Calendar.getInstance();
        endDateTime.set(2027, Calendar.MAY, 1, 22, 15);

        Long serviceId = 3L;
        Reservation reservation1 = new Reservation(
            1L, eventService.getEvent(eventId), serviceService.getService(serviceId), startDateTime, endDateTime
        );

        startDateTime.set(2035, Calendar.DECEMBER, 24, 8, 0);
        endDateTime.set(2035, Calendar.DECEMBER, 24, 11, 30);

        Reservation reservation2 = new Reservation(
            2L, eventService.getEvent(eventId), serviceService.getService(serviceId), startDateTime, endDateTime
        );

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);

        return reservations;
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservation;
    }

    public List<Reservation> getReservationByProviderBetween(Long providerId, LocalDate startDateTime, LocalDate endDateTime) {
        Calendar startDateTimeDate = Calendar.getInstance();
        Calendar endDateTimeDate = Calendar.getInstance();
        startDateTimeDate.setTime(Date.from(startDateTime.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        endDateTimeDate.setTime(Date.from(endDateTime.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return reservationRepository.findReservationsByProvider(providerId, startDateTimeDate, endDateTimeDate);
    }
}
