package org.example.eventy.solutions.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.validation.annotation.ValidReservation;

import java.time.LocalDateTime;

@ValidReservation // trigger the custom validation
public class ReservationDTO {
    @NotNull(message = "Event cannot be null")
    private Long selectedEventId;

    @NotNull(message = "Service cannot be null")
    private Long selectedServiceId;

    @NotNull(message = "Reservation start time cannot be null")
    @Future(message = "Reservation start time must be in the future")
    private LocalDateTime reservationStartDateTime;

    @NotNull(message = "Reservation end time cannot be null")
    @Future(message = "Reservation end time must be in the future")
    private LocalDateTime reservationEndDateTime;

    ///////////////////////////////

    public ReservationDTO() {

    }

    public ReservationDTO(Reservation reservation) {
        this.selectedEventId = reservation.getSelectedEvent().getId();
        this.selectedServiceId = reservation.getSelectedService().getId();
        this.reservationStartDateTime = reservation.getReservationStartDateTime();
        this.reservationEndDateTime = reservation.getReservationEndDateTime();
    }

    public ReservationDTO(Long selectedEventId, Long selectedServiceId, LocalDateTime reservationStartDateTime, LocalDateTime reservationEndDateTime) {
        this.selectedEventId = selectedEventId;
        this.selectedServiceId = selectedServiceId;
        this.reservationStartDateTime = reservationStartDateTime;
        this.reservationEndDateTime = reservationEndDateTime;
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
