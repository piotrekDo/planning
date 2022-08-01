package com.piotrdomagalski.planning.app;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DigitsOnlyValidator.class)
public @interface DigitsOnly {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

