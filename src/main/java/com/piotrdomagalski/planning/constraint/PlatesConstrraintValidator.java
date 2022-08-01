package com.piotrdomagalski.planning.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PlatesConstrraintValidator implements ConstraintValidator<PlatesConstrraint, String> {
    @Override
    public void initialize(PlatesConstrraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }

        return s.matches("^[a-zA-Z][a-zA-Z0-9]([a-zA-Z0-9])*");
    }
}
