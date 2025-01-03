package org.example.eventy.users.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.eventy.users.validation.validator.UpgradeProfileValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// custom annotation for Upgrade Profile validations
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UpgradeProfileValidator.class)
public @interface ValidUpgradeProfile {
    String message() default "Invalid profile upgrade"; // default message (you can override this)
    Class<?>[] groups() default {}; // grouping constraints
    Class<? extends Payload>[] payload() default {}; // additional data
}