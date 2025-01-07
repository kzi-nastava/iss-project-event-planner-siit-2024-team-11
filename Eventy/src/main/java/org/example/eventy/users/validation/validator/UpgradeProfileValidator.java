package org.example.eventy.users.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.users.dtos.UpgradeProfileDTO;
import org.example.eventy.users.services.UserService;
import org.example.eventy.users.validation.annotation.ValidUpgradeProfile;
import org.springframework.beans.factory.annotation.Autowired;

public class UpgradeProfileValidator implements ConstraintValidator<ValidUpgradeProfile, UpgradeProfileDTO> {
    @Autowired
    private UserService userService;

    public UpgradeProfileValidator() {
    }

    @Override
    public boolean isValid(UpgradeProfileDTO upgradeProfileDTO, ConstraintValidatorContext context) {
        // 1. Check email
        if (userService.getUserByEmail(upgradeProfileDTO.getEmail()) == null) {
            context.buildConstraintViolationWithTemplate("An user with this email doesn't exists")
                    .addConstraintViolation();
            return false;
        }

        // 2. Check account type
        if (!upgradeProfileDTO.getAccountType().equals("EVENT ORGANIZER") || !upgradeProfileDTO.getAccountType().equals("SOLUTIONS PROVIDER")) {
            context.buildConstraintViolationWithTemplate("Account type is not valid.")
                    .addPropertyNode("accountType")
                    .addConstraintViolation();
            return false;
        }

        if (upgradeProfileDTO.getAccountType().equals("EVENT ORGANIZER")) {
            // 3.1 Check first name
            if (upgradeProfileDTO.getFirstName() == null) {
                context.buildConstraintViolationWithTemplate("First name cannot be empty.")
                        .addPropertyNode("firstName")
                        .addConstraintViolation();
                return false;
            }

                // first name only letters
            if (!upgradeProfileDTO.getFirstName().matches("^[a-zA-Z]+$")) {
                context.buildConstraintViolationWithTemplate("First name must contain only letters.")
                        .addPropertyNode("firstName")
                        .addConstraintViolation();
                return false;
            }

            // 3.2 Check last name
            if (upgradeProfileDTO.getLastName() == null) {
                context.buildConstraintViolationWithTemplate("Last name cannot be empty.")
                        .addPropertyNode("lastName")
                        .addConstraintViolation();
                return false;
            }

                // last name only letters
            if (!upgradeProfileDTO.getLastName().matches("^[a-zA-Z]+$")) {
                context.buildConstraintViolationWithTemplate("Last name must contain only letters.")
                        .addPropertyNode("lastName")
                        .addConstraintViolation();
                return false;
            }

        } else {
            // 3.1 Check company name
            if (upgradeProfileDTO.getCompanyName() == null) {
                context.buildConstraintViolationWithTemplate("Company name cannot be empty.")
                        .addPropertyNode("companyName")
                        .addConstraintViolation();
                return false;
            }

                // company name only letters
            if (!upgradeProfileDTO.getCompanyName().matches("^[a-zA-Z]+$")) {
                context.buildConstraintViolationWithTemplate("Company name must contain only letters.")
                        .addPropertyNode("companyName")
                        .addConstraintViolation();
                return false;
            }

            // 3.2 Check description
            if (upgradeProfileDTO.getDescription() == null) {
                context.buildConstraintViolationWithTemplate("Description cannot be empty.")
                        .addPropertyNode("description")
                        .addConstraintViolation();
                return false;
            }

                // description only letters
            if (!upgradeProfileDTO.getDescription().matches("^[a-zA-Z]+$")) {
                context.buildConstraintViolationWithTemplate("Description must contain only letters.")
                        .addPropertyNode("description")
                        .addConstraintViolation();
                return false;
            }
        }

        // 4. Check picture/s
        if (upgradeProfileDTO.getProfilePictures() == null || upgradeProfileDTO.getProfilePictures().isEmpty()) {
            context.buildConstraintViolationWithTemplate("Profile pictures list cannot be empty.")
                    .addPropertyNode("profilePictures")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
