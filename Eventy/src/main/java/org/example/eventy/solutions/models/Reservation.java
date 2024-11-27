package org.example.eventy.solutions.models;

import org.example.eventy.events.models.Event;

import java.util.Calendar;

public class Reservation {
    private Long id;
    private Event selectedEvent;
    private Solution selectedService;
    private Calendar reservationStartDateTime;
    private Calendar reservationEndDateTime;

    public Reservation() {
    }

    public Reservation(Long id, Event selectedEvent, Solution selectedService, Calendar reservationStartDateTime, Calendar reservationEndDateTime) {
        this.id = id;
        this.selectedEvent = selectedEvent;
        this.selectedService = selectedService;
        this.reservationStartDateTime = reservationStartDateTime;
        this.reservationEndDateTime = reservationEndDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Solution getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(Solution selectedService) {
        this.selectedService = selectedService;
    }

    public Calendar getReservationStartDateTime() {
        return reservationStartDateTime;
    }

    public void setReservationStartDateTime(Calendar reservationStartDateTime) {
        this.reservationStartDateTime = reservationStartDateTime;
    }

    public Calendar getReservationEndDateTime() {
        return reservationEndDateTime;
    }

    public void setReservationEndDateTime(Calendar reservationEndDateTime) {
        this.reservationEndDateTime = reservationEndDateTime;
    }
}
