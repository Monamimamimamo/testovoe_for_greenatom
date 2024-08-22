package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Base64;

public class Base64FileValidator implements ConstraintValidator<Base64File, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Base64.getDecoder().decode(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
