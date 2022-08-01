package com.piotrdomagalski.planning.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserNameConstraintValidator implements ConstraintValidator<UserNameConstraint, String> {
    @Override
    public void initialize(UserNameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile("^(?!_)(?!.*[\\W\\s].*)(?!.*__.*)[a-zA-z]{3}\\w*.*(?<!_)$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
