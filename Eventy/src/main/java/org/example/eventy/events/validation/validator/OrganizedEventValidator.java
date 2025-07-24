package org.example.eventy.events.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.events.dtos.CreateActivityDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.regex.Pattern;

public class OrganizedEventValidator implements ConstraintValidator<ValidOrganizedEvent, OrganizeEventDTO> {
    @Autowired
    private UserService userService;
    @Autowired
    private EventTypeService eventTypeService;

    public OrganizedEventValidator() {
    }

    @Override
    public boolean isValid(OrganizeEventDTO organizeEventDTO, ConstraintValidatorContext context) {
                    // NOTE: return early "false" if something is not valid!

        if (organizeEventDTO.getName().length() > 20) {
            return false;
        }

        if(eventTypeService.get(organizeEventDTO.getEventTypeId()) == null) {
            context.buildConstraintViolationWithTemplate("Selected event type does not exist")
                    .addPropertyNode("eventTypeId")
                    .addConstraintViolation();
            return false;
        }

        // 8. Check if "agenda" is okay - @NotNull is already checked in OrganizeEventDTO
        for(CreateActivityDTO activityDTO : organizeEventDTO.getAgenda()) {
            if(activityDTO.getStartTime().isAfter(activityDTO.getEndTime()) || activityDTO.getStartTime().isEqual(activityDTO.getEndTime()) ||
                    activityDTO.getStartTime().isBefore(organizeEventDTO.getDate()) || activityDTO.getEndTime().isAfter(organizeEventDTO.getDate().plusDays(1))) {
                return false;
            }
        }

        // 9. Check if "emails" is okay - if EventPrivacy == PRIVATE
        if (!organizeEventDTO.getIsPublic()) {
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
