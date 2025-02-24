package org.example.eventy.users.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.eventy.users.validation.validator.BlockUserValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// custom annotation for Block User validations
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BlockUserValidator.class)
public @interface ValidBlockUser {
    String message() default "Invalid block user"; // default message (you can override this)
    Class<?>[] groups() default {}; // grouping constraints
    Class<? extends Payload>[] payload() default {}; // additional data
}