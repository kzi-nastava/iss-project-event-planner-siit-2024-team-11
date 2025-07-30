package org.example.eventy.events.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.events.dtos.CreateActivityDTO;
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
    private EventTypeService eventTypeService;

    public UpdateEventValidator() {}

    @Override
    public boolean isValid(UpdateEventDTO updateEventDTO, ConstraintValidatorContext context) {
        if (updateEventDTO.getName().length() > 20) {
            return false;
        }

        if(eventTypeService.get(updateEventDTO.getEventTypeId()) == null) {
            context.buildConstraintViolationWithTemplate("Selected event type does not exist")
                    .addPropertyNode("eventTypeId")
                    .addConstraintViolation();
            return false;
        }

        // 8. Check if "agenda" is okay - @NotNull is already checked in OrganizeEventDTO
        for(CreateActivityDTO activityDTO : updateEventDTO.getAgenda()) {
            if(activityDTO.getStartTime().isAfter(activityDTO.getEndTime()) || activityDTO.getStartTime().isEqual(activityDTO.getEndTime()) ||
                    activityDTO.getStartTime().isBefore(updateEventDTO.getDate()) || activityDTO.getEndTime().isAfter(updateEventDTO.getDate().plusDays(1))) {
                return false;
            }
        }

        return true;
    }
}
