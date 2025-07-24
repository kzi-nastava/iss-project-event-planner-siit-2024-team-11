package org.example.eventy.users.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.eventy.users.validation.validator.RegistrationValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegistrationValidator.class)
public @interface ValidRegistration {
    String message() default "Invalid registration!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
