package org.example.eventy.reviews.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.eventy.reviews.validation.validator.CreateReviewValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// custom annotation for Upgrade Profile validations
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreateReviewValidator.class)
public @interface ValidCreateReview {
    String message() default "Invalid create review"; // default message (you can override this)
    Class<?>[] groups() default {}; // grouping constraints
    Class<? extends Payload>[] payload() default {}; // additional data
}