package org.example.eventy.solutions.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.dtos.ReservationDTO;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.services.ReservationService;
import org.example.eventy.solutions.services.ServiceService;
import org.example.eventy.solutions.validation.annotation.ValidReservation;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;

public class ReservationValidator implements ConstraintValidator<ValidReservation, ReservationDTO> {
    @Autowired
    private EventService eventService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ReservationService reservationService;

    public ReservationValidator() {
    }

    @Override
    public boolean isValid(ReservationDTO reservationDTO, ConstraintValidatorContext context) {
        // 1. Check if the selected event exists
        if (eventService.getEvent(reservationDTO.getSelectedEventId()) == null) {
            context.buildConstraintViolationWithTemplate("Selected event does not exist")
                    .addPropertyNode("selectedEventId")
                    .addConstraintViolation();
            return false;
        }

        // 2. Check if the selected service exists
        if (serviceService.getService(reservationDTO.getSelectedServiceId()) == null) {
            context.buildConstraintViolationWithTemplate("Selected service does not exist")
                    .addPropertyNode("selectedServiceId")
                    .addConstraintViolation();
            return false;
        }

        // 3. Check if the reservation start time is in the future
        if (reservationDTO.getReservationStartDateTime().isBefore(LocalDateTime.now())) {
            context.buildConstraintViolationWithTemplate("Reservation start time must be in the future")
                    .addPropertyNode("reservationStartDateTime")
                    .addConstraintViolation();
            return false;
        }

        // 4. Check if the reservation end time is after the start time
        if (reservationDTO.getReservationEndDateTime().isBefore(reservationDTO.getReservationStartDateTime())) {
            context.buildConstraintViolationWithTemplate("Reservation end time must be after start time")
                    .addPropertyNode("reservationEndDateTime")
                    .addConstraintViolation();
            return false;
        }

        // 5. Check if the duration is invalid
        Service service = (Service) serviceService.getService(reservationDTO.getSelectedServiceId());
        Duration duration = Duration.between(reservationDTO.getReservationStartDateTime(), reservationDTO.getReservationEndDateTime());
        if (duration.toMinutes() < service.getMinReservationTime() || duration.toMinutes() > service.getMaxReservationTime()) {
            String message = "Reservation duration must be between " + service.getMinReservationTime() + " and " + service.getMaxReservationTime() + " minutes";
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }

        // 6. Check if the reservation overlaps with any existing reservations
        /*List<Reservation> overlappingReservations = reservationService.findOverlappingReservations(reservation);
        if (!overlappingReservations.isEmpty()) {
            context.buildConstraintViolationWithTemplate("The selected time overlaps with an existing reservation")
                    .addPropertyNode("reservationStartDateTime")
                    .addConstraintViolation();
            valid = false;
        }
        */

        return true;
    }
}