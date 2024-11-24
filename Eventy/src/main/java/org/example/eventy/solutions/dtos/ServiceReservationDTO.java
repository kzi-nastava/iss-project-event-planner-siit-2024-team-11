package org.example.eventy.solutions.dtos;

import java.util.Calendar;

public class ServiceReservationDTO {
    private Long selectedEventId;
    private Long selectedSolutionId;
    private Calendar reservationStartDateTime;
    private Calendar reservationEndDateTime;

    public ServiceReservationDTO() {
    }

    public ServiceReservationDTO(Long selectedEventId, Long selectedSolutionId, Calendar reservationStartDateTime, Calendar reservationEndDateTime) {
        this.selectedEventId = selectedEventId;
        this.selectedSolutionId = selectedSolutionId;
        this.reservationStartDateTime = reservationStartDateTime;
        this.reservationEndDateTime = reservationEndDateTime;
    }

    public Long getSelectedEventId() {
        return selectedEventId;
    }

    public void setSelectedEventId(Long selectedEventId) {
        this.selectedEventId = selectedEventId;
    }

    public Long getSelectedSolutionId() {
        return selectedSolutionId;
    }

    public void setSelectedSolutionId(Long selectedSolutionId) {
        this.selectedSolutionId = selectedSolutionId;
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
