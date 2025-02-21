package org.example.eventy.users.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.services.SolutionService;
import org.example.eventy.users.dtos.CreateReportDTO;
import org.example.eventy.users.services.UserService;
import org.example.eventy.users.validation.annotation.ValidCreateReport;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateReportValidator implements ConstraintValidator<ValidCreateReport, CreateReportDTO> {
    @Autowired
    private UserService userService;

    public CreateReportValidator() {}

    @Override
    public boolean isValid(CreateReportDTO createReportDTO, ConstraintValidatorContext context) {
        // 1. Check user
        if (userService.get(createReportDTO.getSenderUserId()) == null) {
            context.buildConstraintViolationWithTemplate("An user with this id doesn't exists.")
                    .addPropertyNode("senderUserId")
                    .addConstraintViolation();
            return false;
        }

        if (userService.get(createReportDTO.getReportedUserId()) == null) {
            context.buildConstraintViolationWithTemplate("An user with this id doesn't exists.")
                    .addPropertyNode("reportedUserId")
                    .addConstraintViolation();
            return false;
        }

        if (createReportDTO.getReason().isEmpty() || createReportDTO.getReason() == null) {
            context.buildConstraintViolationWithTemplate("The reason cannot be empty.")
                    .addPropertyNode("reason")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
