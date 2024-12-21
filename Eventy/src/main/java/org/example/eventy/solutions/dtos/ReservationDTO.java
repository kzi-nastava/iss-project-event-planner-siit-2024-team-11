package org.example.eventy.solutions.dtos;

import org.example.eventy.solutions.models.Reservation;

import java.time.LocalDateTime;
import java.util.Calendar;

public class ReservationDTO {
    private Long id;
    private Long selectedEventId;
    private Long selectedServiceId;
    private LocalDateTime reservationStartDateTime;
    private LocalDateTime reservationEndDateTime;

    public ReservationDTO() {

    }

    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.selectedEventId = reservation.getSelectedEvent().getId();
        this.selectedServiceId = reservation.getSelectedService().getId();
        this.reservationStartDateTime = reservation.getReservationStartDateTime();
        this.reservationEndDateTime = reservation.getReservationEndDateTime();
    }

    public ReservationDTO(Long id, Long selectedEventId, Long selectedServiceId, LocalDateTime reservationStartDateTime, LocalDateTime reservationEndDateTime) {
        this.id = id;
        this.selectedEventId = selectedEventId;
        this.selectedServiceId = selectedServiceId;
        this.reservationStartDateTime = reservationStartDateTime;
        this.reservationEndDateTime = reservationEndDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSelectedEventId() {
        return selectedEventId;
    }

    public void setSelectedEventId(Long selectedEventId) {
        this.selectedEventId = selectedEventId;
    }

    public Long getSelectedServiceId() {
        return selectedServiceId;
    }

    public void setSelectedServiceId(Long selectedServiceId) {
        this.selectedServiceId = selectedServiceId;
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
