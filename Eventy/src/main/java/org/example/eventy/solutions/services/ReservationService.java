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
        return null;
    }

    public ArrayList<Reservation> getReservationsByServiceId(Long serviceId) {
        return null;
    }

    public ArrayList<Reservation> getReservationsByEventId(Long eventId) {
        return null;
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservation;
    }

    public List<Reservation> getReservationByProviderBetween(Long providerId, LocalDate startDateTime, LocalDate endDateTime) {
        return reservationRepository.findReservationsByProvider(providerId, startDateTime.atStartOfDay(), endDateTime.atStartOfDay());
    }
}
