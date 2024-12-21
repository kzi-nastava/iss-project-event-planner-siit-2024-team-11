package org.example.eventy.solutions.models;

import jakarta.persistence.*;
import org.example.eventy.events.models.Event;

import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Table(name = "Reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "selected_event_id", referencedColumnName = "id")
    private Event selectedEvent;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "selected_service_id", referencedColumnName = "id")
    private Solution selectedService;
    @Column(nullable = false)
    private LocalDateTime reservationStartDateTime;
    @Column(nullable = false)
    private LocalDateTime reservationEndDateTime;

    public Reservation() {
    }

    public Reservation(Long id, Event selectedEvent, Solution selectedService, LocalDateTime reservationStartDateTime, LocalDateTime reservationEndDateTime) {
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

    public LocalDateTime getReservationStartDateTime() {
        return reservationStartDateTime;
    }

    public void setReservationStartDateTime(LocalDateTime reservationStartDateTime) {
        this.reservationStartDateTime = reservationStartDateTime;
    }

    public LocalDateTime getReservationEndDateTime() {
        return reservationEndDateTime;
    }

    public void setReservationEndDateTime(LocalDateTime reservationEndDateTime) {
        this.reservationEndDateTime = reservationEndDateTime;
    }
}
