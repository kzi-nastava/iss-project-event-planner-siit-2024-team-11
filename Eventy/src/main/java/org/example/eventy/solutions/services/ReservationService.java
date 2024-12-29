package org.example.eventy.solutions.services;

import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.dtos.ReservationDTO;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.eventy.solutions.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).orElse(null);
    }

    public ArrayList<Reservation> getReservationsByServiceId(Long serviceId) {
        Solution service = serviceService.getService(serviceId);
        return reservationRepository.findAllBySelectedService(service);
    }

    public ArrayList<Reservation> getReservationsByEventId(Long eventId) {
        Event event = eventService.getEvent(eventId);
        return reservationRepository.findAllBySelectedEvent(event);
    }

    public Page<Reservation> getReservationsByUserId(Long userId, Pageable pageable) {
        return reservationRepository.findAllByUserId(userId, pageable);
    }

    public Reservation createReservation(ReservationDTO reservation) {
        Reservation newReservation = new Reservation();
        newReservation.setSelectedEvent(eventService.getEvent(reservation.getSelectedEventId()));
        newReservation.setSelectedService(serviceService.getService(reservation.getSelectedServiceId()));
        newReservation.setReservationStartDateTime(reservation.getReservationStartDateTime());
        newReservation.setReservationEndDateTime(reservation.getReservationEndDateTime());

        return saveReservation(newReservation);
    }

    public List<Reservation> findOverlappingReservations(ReservationDTO newReservation) {
        LocalDateTime start = newReservation.getReservationStartDateTime();
        LocalDateTime end = newReservation.getReservationEndDateTime();

        return reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfter(
                end, start);
    }

    public Reservation saveReservation(Reservation reservation) {
        try {
            return reservationRepository.save(reservation);
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<Reservation> getReservationByProviderBetween(Long providerId, LocalDate startDateTime, LocalDate endDateTime) {
        return reservationRepository.findReservationsByProvider(providerId, startDateTime.atStartOfDay(), endDateTime.atStartOfDay());
    }
}
