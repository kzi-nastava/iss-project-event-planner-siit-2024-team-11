package org.example.eventy.users.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.eventy.users.dtos.BlockUserDTO;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.example.eventy.users.validation.annotation.ValidBlockUser;
import org.springframework.beans.factory.annotation.Autowired;

public class BlockUserValidator implements ConstraintValidator<ValidBlockUser, BlockUserDTO> {
    @Autowired
    private UserService userService;

    public BlockUserValidator() {}

    @Override
    public boolean isValid(BlockUserDTO blockUserDTO, ConstraintValidatorContext context) {
        // 1. Check user
        User blockerUser = userService.get(blockUserDTO.getBlockerId());
        if (blockerUser == null) {
            context.buildConstraintViolationWithTemplate("An user with this id doesn't exists.")
                    .addPropertyNode("blockerId")
                    .addConstraintViolation();
            return false;
        }

        User blockedUser = userService.get(blockUserDTO.getBlockedId());
        if (blockedUser == null) {
            context.buildConstraintViolationWithTemplate("An user with this id doesn't exists.")
                    .addPropertyNode("blockedId")
                    .addConstraintViolation();
            return false;
        }

        if (blockerUser.getId().equals(blockedUser.getId())) {
            context.buildConstraintViolationWithTemplate("You cannot block yourself.")
                    .addPropertyNode("blockerId")
                    .addConstraintViolation();
            return false;
        }

        if (blockerUser.getBlocked().contains(blockedUser)) {
            context.buildConstraintViolationWithTemplate("This user is already blocked.")
                    .addPropertyNode("blockedId")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
