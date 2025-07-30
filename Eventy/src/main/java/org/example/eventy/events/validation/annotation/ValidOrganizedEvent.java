package org.example.eventy.events.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.eventy.events.validation.validator.OrganizedEventValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// custom annotation for Organized Event validations
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrganizedEventValidator.class)
public @interface ValidOrganizedEvent {
    String message() default "Invalid organized event"; // default message (you can override this)
    Class<?>[] groups() default {}; // grouping constraints
    Class<? extends Payload>[] payload() default {}; // additional data
}