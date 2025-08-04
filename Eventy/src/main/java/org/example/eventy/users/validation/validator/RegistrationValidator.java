package org.example.eventy.users.validation.validator;

import jakarta.validation.ConstraintValidator;
import org.example.eventy.users.dtos.RegistrationDTO;
import org.example.eventy.users.validation.annotation.ValidRegistration;

import java.util.Objects;

public class RegistrationValidator implements ConstraintValidator<ValidRegistration, RegistrationDTO> {
    @Override
    public boolean isValid(RegistrationDTO registrationDTO, jakarta.validation.ConstraintValidatorContext context) {
        if (!isOrganizer(registrationDTO) && !isProvider(registrationDTO)) {
            return false;
        }

        if (!Objects.equals(registrationDTO.getPassword(), registrationDTO.getConfirmedPassword())) {
            return false;
        }

        if (!isPhoneNumber(registrationDTO.getPhoneNumber())) {
            return false;
        }

        if (!isEmail(registrationDTO.getEmail())) {
            return false;
        }

        return true;
    }

    private boolean isOrganizer(RegistrationDTO registrationDTO) {
        return registrationDTO.getFirstName() != null && registrationDTO.getLastName() != null &&
                registrationDTO.getName() == null && registrationDTO.getDescription() == null;
    }

    private boolean isProvider(RegistrationDTO registrationDTO) {
        return  registrationDTO.getName() != null && registrationDTO.getDescription() != null &&
                registrationDTO.getFirstName() == null && registrationDTO.getLastName() == null;
    }

    private boolean isPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+?[0-9]{1,4}?[-.\\s]?(\\(?\\d{1,5}\\)?[-.\\s]?)*\\d{1,6}$");
    }

    private boolean isEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}
