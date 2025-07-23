package org.example.eventy.solutions.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.eventy.solutions.validation.validator.ServiceValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ServiceValidator.class)
public @interface ValidService {
    String message() default "Invalid service";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
