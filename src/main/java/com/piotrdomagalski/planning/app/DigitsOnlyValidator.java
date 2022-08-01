package com.piotrdomagalski.planning.app;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DigitsOnlyValidator implements ConstraintValidator<DigitsOnly, String> {

    @Override
    public void initialize(DigitsOnly constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}