package org.example.eventy.solutions.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.example.eventy.events.models.Event;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "selected_event_id", referencedColumnName = "id")
    @NotNull(message = "Event must not be null")
    private Event selectedEvent;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "selected_service_id", referencedColumnName = "id")
    @NotNull(message = "Service must not be null")
    private Solution selectedService;

    @Column(nullable = false)
    @NotNull(message = "Reservation start time cannot be null")
    @Future(message = "Reservation start time must be in the future")
    private LocalDateTime reservationStartDateTime;

    @Column(nullable = false)
    @NotNull(message = "Reservation end time cannot be null")
    @Future(message = "Reservation end time must be in the future")
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
