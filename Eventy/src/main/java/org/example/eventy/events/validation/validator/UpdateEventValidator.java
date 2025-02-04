package org.example.eventy.events.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.events.dtos.UpdateEventDTO;
import org.example.eventy.events.services.EventService;
import org.example.eventy.events.services.EventTypeService;
import org.example.eventy.events.validation.annotation.ValidUpdateEvent;
import org.example.eventy.solutions.services.ReservationService;
import org.example.eventy.solutions.services.ServiceService;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateEventValidator implements ConstraintValidator<ValidUpdateEvent, UpdateEventDTO> {
    @Autowired
    private EventService eventService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventTypeService eventTypeService;

    public UpdateEventValidator() {}

    @Override
    public boolean isValid(UpdateEventDTO updateEventDTO, ConstraintValidatorContext context) {
                    // NOTE: return early "false" if something is not valid!

        // 1. Check if "name" is okay - @NotNull is already checked in UpdateEventDTO
        //    also put max lenght about 20 characters? because of cards display, string would be too long to fit..

        // 2. Check if "description" is okay - @NotNull is already checked in UpdateEventDTO

        // 3. Check if "maxNumberParticipants" is okay - @NotNull is already checked in UpdateEventDTO
        //    maybe put maxNumberParticipants <= 9999 --> too long to fit into card view

        // 4. Check if "isPublic" is okay - @NotNull is already checked in UpdateEventDTO

        // 5. Check if "eventTypeId" is okay - @NotNull is already checked in UpdateEventDTO
        //    check if event type exists?
            /* ------ NOTE ------ : HERE IS MY EXAMPLE previous validator, just put event types instead of event
            // 1. Check if the selected event exists
            if (eventService.getEvent(reservationDTO.getSelectedEventId()) == null) {
                context.buildConstraintViolationWithTemplate("Selected event does not exist")
                       .addPropertyNode("selectedEventId")   // property is the field from DTO which would be invalid ---> "eventTypeId"
                       .addConstraintViolation();
                return false;
            }
            */

        // 6. Check if "location" is okay - @NotNull is already checked in UpdateEventDTO

        // 7. Check if "date" is okay - @NotNull and @Future is already checked in UpdateEventDTO
        //    @Future - any day after current time, for example 21.10.2022. 00:00 ---> 21.10.2022. 00:01 is okay
        //    probably should add some time between, a day for example or 7 days!
            /* ------ NOTE ------ : HERE IS MY EXAMPLE:
            // 4. Check if the reservation start time is in the future
            if (reservationDTO.getReservationStartDateTime().isBefore(LocalDateTime.now().plusDays(selectedService.getReservationDeadline()))) {
                context.buildConstraintViolationWithTemplate("It's too late to make a reservation")
                       .addPropertyNode("reservationStartDateTime")
                       .addConstraintViolation();
                return false;
            }
            */

        // 8. Check if "agenda" is okay - @NotNull is already checked in UpdateEventDTO

        return true;
    }
}
