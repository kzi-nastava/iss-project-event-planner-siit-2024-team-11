package org.example.eventy.reviews.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.events.services.EventService;
import org.example.eventy.reviews.dtos.CreateReviewDTO;
import org.example.eventy.reviews.validation.annotation.ValidCreateReview;
import org.example.eventy.solutions.services.SolutionService;
import org.example.eventy.users.dtos.UpgradeProfileDTO;
import org.example.eventy.users.services.UserService;
import org.example.eventy.users.validation.annotation.ValidUpgradeProfile;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateReviewValidator implements ConstraintValidator<ValidCreateReview, CreateReviewDTO> {
    @Autowired
    private UserService userService;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private EventService eventService;

    public CreateReviewValidator() {
    }

    @Override
    public boolean isValid(CreateReviewDTO createReviewDTO, ConstraintValidatorContext context) {
        // 1. Check user
        if (userService.get(createReviewDTO.getGraderId()) == null) {
            context.buildConstraintViolationWithTemplate("An user with this id doesn't exists.")
                    .addPropertyNode("graderId")
                    .addConstraintViolation();
            return false;
        }

        if (createReviewDTO.getSolutionId() != null) {
            if (solutionService.getSolution(createReviewDTO.getSolutionId()) == null) {
                context.buildConstraintViolationWithTemplate("A solution with this id doesn't exists.")
                        .addPropertyNode("solutionId")
                        .addConstraintViolation();
                return false;
            }
        }

        if (createReviewDTO.getEventId() != null) {
            if (eventService.getEvent(createReviewDTO.getEventId()) == null) {
                context.buildConstraintViolationWithTemplate("An event with this id doesn't exists.")
                        .addPropertyNode("eventId")
                        .addConstraintViolation();
                return false;
            }
        }

        if (createReviewDTO.getSolutionId() == null && createReviewDTO.getEventId() == null) {
            context.buildConstraintViolationWithTemplate("Review must be either for solution or event, cannot be for neither.")
                    .addPropertyNode("solutionId")
                    .addConstraintViolation();
            return false;
        }

        try {
            Integer grade = createReviewDTO.getGrade();
            if (grade < 1 || grade > 5) {
                context.buildConstraintViolationWithTemplate("The grade must be an Integer between 1 and 5 (including 1 and 5).")
                        .addPropertyNode("grade")
                        .addConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            context.buildConstraintViolationWithTemplate("The grade must be an Integer between 1 and 5 (including 1 and 5).")
                    .addPropertyNode("grade")
                    .addConstraintViolation();
            return false;
        }


        if (createReviewDTO.getComment().isEmpty() || createReviewDTO.getComment() == null) {
            context.buildConstraintViolationWithTemplate("The message cannot be empty.")
                    .addPropertyNode("comment")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
