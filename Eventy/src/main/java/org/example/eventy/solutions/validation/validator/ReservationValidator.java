package org.example.eventy.solutions.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.dtos.ReservationDTO;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.services.ReservationService;
import org.example.eventy.solutions.services.ServiceService;
import org.example.eventy.solutions.validation.annotation.ValidReservation;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationValidator implements ConstraintValidator<ValidReservation, ReservationDTO> {
    @Autowired
    private EventService eventService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ReservationService reservationService;

    public ReservationValidator() {}

    @Override
    public boolean isValid(ReservationDTO reservationDTO, ConstraintValidatorContext context) {
        // 1. Check if the selected event exists
        Event selectedEvent = eventService.getEvent(reservationDTO.getSelectedEventId());

        if (selectedEvent == null) {
            context.buildConstraintViolationWithTemplate("Selected event does not exist.")
                    .addPropertyNode("selectedEventId")
                    .addConstraintViolation();
            return false;
        }

        // 2. Check if the selected service exists
        if (serviceService.getService(reservationDTO.getSelectedServiceId()) == null) {
            context.buildConstraintViolationWithTemplate("Selected service does not exist.")
                    .addPropertyNode("selectedServiceId")
                    .addConstraintViolation();
            return false;
        }

        Service selectedService = (Service) serviceService.getService(reservationDTO.getSelectedServiceId());

        // 3. Check if the selected service is available
        if (!selectedService.isAvailable()) {
            context.buildConstraintViolationWithTemplate("Selected service is not available.")
                    .addPropertyNode("selectedServiceId")
                    .addConstraintViolation();
            return false;
        }

        // 4. Check if the reservation start time is in the future
        if (reservationDTO.getReservationStartDateTime().isBefore(LocalDateTime.now().plusDays(selectedService.getReservationDeadline()))) {
            context.buildConstraintViolationWithTemplate("It's too late to make a reservation.")
                    .addPropertyNode("reservationStartDateTime")
                    .addConstraintViolation();
            return false;
        }

        // 5. Check if the reservation end time is after the start time
        if (reservationDTO.getReservationEndDateTime().isBefore(reservationDTO.getReservationStartDateTime())) {
            context.buildConstraintViolationWithTemplate("Reservation end time must be after start time.")
                    .addPropertyNode("reservationEndDateTime")
                    .addConstraintViolation();
            return false;
        }

        // 6. Check if the duration is invalid
        Duration duration = Duration.between(reservationDTO.getReservationStartDateTime(), reservationDTO.getReservationEndDateTime());
        if (duration.toMinutes() < selectedService.getMinReservationTime() || duration.toMinutes() > selectedService.getMaxReservationTime()) {
            String message = "Reservation duration must be between " + selectedService.getMinReservationTime() + " and " + selectedService.getMaxReservationTime() + " minutes.";
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("reservationStartDateTime")
                    .addConstraintViolation();
            return false;
        }

        // 7. Check if the reservation overlaps with any existing reservations
        List<Reservation> overlappingReservations = reservationService.findOverlappingReservations(reservationDTO);
        if (!overlappingReservations.isEmpty()) {
            context.buildConstraintViolationWithTemplate("The selected time overlaps with an existing reservation.")
                    .addPropertyNode("reservationStartDateTime")
                    .addConstraintViolation();
            return false;
        }

        // 8. Check for consistency between "From" and "To" fields if the service duration is fixed
        if (selectedService.getMinReservationTime().intValue() == selectedService.getMaxReservationTime().intValue()) {
            LocalDateTime calculatedEndTime = reservationDTO.getReservationStartDateTime().plusMinutes(selectedService.getMaxReservationTime());
            if (!reservationDTO.getReservationEndDateTime().equals(calculatedEndTime)) {
                context.buildConstraintViolationWithTemplate("End time does not match the calculated duration of the service.")
                        .addPropertyNode("reservationEndDateTime")
                        .addConstraintViolation();
                return false;
            }
        }

        // 9. Check if the event date is in the past
        if (selectedEvent.getDate().isBefore(LocalDateTime.now())) {
            context.buildConstraintViolationWithTemplate("Selected event is in the past.")
                    .addPropertyNode("selectedEventId")
                    .addConstraintViolation();
            return false;
        }

        // 10. Check if the reservation time is on the event date
        if (reservationDTO.getReservationStartDateTime().getYear() != selectedEvent.getDate().getYear() ||
            reservationDTO.getReservationStartDateTime().getMonth() != selectedEvent.getDate().getMonth() ||
            reservationDTO.getReservationStartDateTime().getDayOfMonth() != selectedEvent.getDate().getDayOfMonth() ||
            reservationDTO.getReservationEndDateTime().getYear() != selectedEvent.getDate().getYear() ||
            reservationDTO.getReservationEndDateTime().getMonth() != selectedEvent.getDate().getMonth() ||
            reservationDTO.getReservationEndDateTime().getDayOfMonth() != selectedEvent.getDate().getDayOfMonth()) {
            context.buildConstraintViolationWithTemplate("Reservation must be the same day as the event start date.")
                .addPropertyNode("reservationStartDateTime")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}