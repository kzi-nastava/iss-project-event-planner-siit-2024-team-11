package org.example.eventy.solutions.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.solutions.dtos.services.CreateServiceDTO;
import org.example.eventy.solutions.validation.annotation.ValidService;

public class ServiceValidator implements ConstraintValidator<ValidService, CreateServiceDTO> {
    @Override
    public boolean isValid(CreateServiceDTO createServiceDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (createServiceDTO.getName() == null || createServiceDTO.getName().isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Name is required").addPropertyNode("name").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getDescription() == null || createServiceDTO.getDescription().isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Description is required").addPropertyNode("description").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getPrice() == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Price is required").addPropertyNode("price").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getPrice() == 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Price can not be zero").addPropertyNode("price").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getPrice() < 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Price can not be negative").addPropertyNode("price").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getDiscount() == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Discount is required").addPropertyNode("discount").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getDiscount() < 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Discount can not be negative").addPropertyNode("discount").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getDiscount() > 100) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Discount can not be over 100%").addPropertyNode("discount").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getImageUrls() == null || createServiceDTO.getImageUrls().isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("At least one image is required").addPropertyNode("imageUrls").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getProviderId() == null || createServiceDTO.getProviderId() < 1) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Invalid provider").addPropertyNode("providerId").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getCategoryId() == null || createServiceDTO.getCategoryId() < 1) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Invalid category").addPropertyNode("categoryId").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getRelatedEventTypeIds() == null || createServiceDTO.getRelatedEventTypeIds().isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("At least one related event type is required").addPropertyNode("relatedEventTypeIds").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getSpecifics() == null || createServiceDTO.getSpecifics().isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Specifics are required").addPropertyNode("specifics").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getMinReservationTime() == null || createServiceDTO.getMinReservationTime() < 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Minimum reservation time is required").addPropertyNode("minReservationTime").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getMaxReservationTime() != null && createServiceDTO.getMaxReservationTime() < createServiceDTO.getMinReservationTime()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Maximum reservation time can not be bigger than minimum reservation time").addPropertyNode("minReservationTime").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getReservationDeadline() == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Reservation deadline is required").addPropertyNode("reservationDeadline").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getReservationDeadline() < 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Reservation deadline can not be negative").addPropertyNode("reservationDeadline").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getCancellationDeadline() == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Cancellation deadline is required").addPropertyNode("cancellationDeadline").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getCancellationDeadline() < 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Cancellation deadline can not be negative").addPropertyNode("cancellationDeadline").addConstraintViolation();
            return false;
        }

        if (createServiceDTO.getAutomaticReservationAcceptance() == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Automatic reservation acceptance is required").addPropertyNode("automaticReservationAcceptance").addConstraintViolation();
            return false;
        }

        return true;
    }
}
