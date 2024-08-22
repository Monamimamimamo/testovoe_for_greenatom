package org.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = Base64FileValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Base64File {
    String message() default "Invalid base64 format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
