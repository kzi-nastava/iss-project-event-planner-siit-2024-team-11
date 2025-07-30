package org.example.eventy.users.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.common.util.EncryptionUtil;
import org.example.eventy.users.dtos.FastRegistrationDTO;
import org.example.eventy.users.services.UserService;
import org.example.eventy.users.validation.annotation.ValidFastRegistration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

public class FastRegistrationValidator implements ConstraintValidator<ValidFastRegistration, FastRegistrationDTO> {
    @Autowired
    private UserService userService;

    public FastRegistrationValidator() {
    }

    @Override
    public boolean isValid(FastRegistrationDTO fastRegistrationDTO, ConstraintValidatorContext context) {
        // 1. Check if email valid
        String email;
        try {
            email = EncryptionUtil.decrypt(fastRegistrationDTO.getEncryptedEmail());
        } catch (Exception e) {
            throw new RuntimeException("Email processing error", e);
        }

            // not good format
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            context.buildConstraintViolationWithTemplate("Wrong email format")
                    .addConstraintViolation();
            return false;
        }

            // email already in use
        if (userService.getUserByEmail(email) != null) {
            context.buildConstraintViolationWithTemplate("Email is already in use")
                    .addConstraintViolation();
            return false;
        }

        // 2. Check if password is good
        if (fastRegistrationDTO.getPassword().length() < 8) {
            context.buildConstraintViolationWithTemplate("Password must be at least 8 characters")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

            // password > 20 ?
        if (fastRegistrationDTO.getPassword().length() > 20) {
            context.buildConstraintViolationWithTemplate("Password must be 20 characters or shorter")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

            // passwords match ?
        if (!fastRegistrationDTO.getPassword().equals(fastRegistrationDTO.getConfirmedPassword())) {
            context.buildConstraintViolationWithTemplate("Passwords must match!")
                    .addPropertyNode("confirmedPassword")
                    .addConstraintViolation();
            return false;
        }

        // 3. Check if address is good
        if (fastRegistrationDTO.getAddress().length() > 20) {
            context.buildConstraintViolationWithTemplate("Address must be 20 characters or shorter")
                    .addPropertyNode("address")
                    .addConstraintViolation();
            return false;
        }

        // 4. Check if phone number is good
        if (userService.getUserByPhoneNumber(fastRegistrationDTO.getPhoneNumber()) != null) {
            context.buildConstraintViolationWithTemplate("Phone number is already taken.")
                    .addPropertyNode("phoneNumber")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}