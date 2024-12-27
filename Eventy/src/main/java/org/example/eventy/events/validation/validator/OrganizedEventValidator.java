package org.example.eventy.events.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.events.dtos.OrganizeEventDTO;
import org.example.eventy.events.services.EventService;
import org.example.eventy.events.services.EventTypeService;
import org.example.eventy.events.validation.annotation.ValidOrganizedEvent;
import org.example.eventy.solutions.services.ReservationService;
import org.example.eventy.solutions.services.ServiceService;
import org.example.eventy.users.dtos.UserType;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.regex.Pattern;

public class OrganizedEventValidator implements ConstraintValidator<ValidOrganizedEvent, OrganizeEventDTO> {
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

    public OrganizedEventValidator() {
    }

    @Override
    public boolean isValid(OrganizeEventDTO organizeEventDTO, ConstraintValidatorContext context) {
                    // NOTE: return early "false" if something is not valid!

        // 1. Check if "name" is okay - @NotNull is already checked in OrganizeEventDTO
        //    also put max lenght about 20 characters? because of cards display, string would be too long to fit..

        // 2. Check if "description" is okay - @NotNull is already checked in OrganizeEventDTO

        // 3. Check if "maxNumberParticipants" is okay - @NotNull is already checked in OrganizeEventDTO
        //    maybe put maxNumberParticipants <= 9999 --> too long to fit into card view

        // 4. Check if "isPublic" is okay - @NotNull is already checked in OrganizeEventDTO

        // 5. Check if "eventTypeId" is okay - @NotNull is already checked in OrganizeEventDTO
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

        // 6. Check if "location" is okay - @NotNull is already checked in OrganizeEventDTO

        // 7. Check if "date" is okay - @NotNull and @Future is already checked in OrganizeEventDTO
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

        // 8. Check if "agenda" is okay - @NotNull is already checked in OrganizeEventDTO

        // 9. Check if "emails" is okay - @NotNull is already checked in OrganizeEventDTO
        List<String> invitedEmails = organizeEventDTO.getEmails();
        if (invitedEmails.isEmpty()) {
            context.buildConstraintViolationWithTemplate("Invited emails cannot be empty")
               .addPropertyNode("emails")
               .addConstraintViolation();
            return false;
        }

        String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        for (String email : invitedEmails) {
            if (email == null || email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
                context.buildConstraintViolationWithTemplate("Invited emails are not valid")
                   .addPropertyNode("emails")
                   .addConstraintViolation();
                return false;
            }
        }

        // 10. Check if "organizerId" is okay - @NotNull is already checked in OrganizeEventDTO
        User organizer = userService.get(organizeEventDTO.getOrganizerId());
        if (organizer == null) {
            context.buildConstraintViolationWithTemplate("Selected organizer does not exist")
                .addPropertyNode("organizerId")   // property is the field from DTO which would be invalid ---> "eventTypeId"
                .addConstraintViolation();
            return false;
        }

        if (userService.getUserType(organizer) != UserType.ORGANIZER) {
            context.buildConstraintViolationWithTemplate("Selected user is not organizer")
                .addPropertyNode("organizerId")   // property is the field from DTO which would be invalid ---> "eventTypeId"
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}
