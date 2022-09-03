package com.piotrdomagalski.planning.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdDocumentConstraintValidator implements ConstraintValidator<IdDocumentConstraint, String> {
    @Override
    public void initialize(IdDocumentConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null)
            return true;

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);

        if (m.find())
            return false;

        if (s.contains(" "))
            return false;

        return s.matches("^[a-zA-Z][a-zA-Z].*");
    }
}
