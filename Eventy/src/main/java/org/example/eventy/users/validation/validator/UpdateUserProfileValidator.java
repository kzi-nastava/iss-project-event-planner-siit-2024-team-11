package org.example.eventy.users.validation.validator;

import jakarta.validation.ConstraintValidator;
import org.example.eventy.users.dtos.UpdateUserProfileDTO;
import org.example.eventy.users.validation.annotation.ValidRegistration;

public class UpdateUserProfileValidator implements ConstraintValidator<ValidRegistration, UpdateUserProfileDTO> {
    @Override
    public boolean isValid(UpdateUserProfileDTO updateUserProfileDTO, jakarta.validation.ConstraintValidatorContext context) {
        if (!updateUserProfileDTO.getPassword().equals(updateUserProfileDTO.getConfirmedPassword())) {
            return false;
        }

        if (!isPhoneNumber(updateUserProfileDTO.getPhoneNumber())) {
            return false;
        }

        return true;
    }

    private boolean isPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+?[0-9]{1,4}?[-.\\s]?(\\(?\\d{1,5}\\)?[-.\\s]?)*\\d{1,6}$");
    }
}
