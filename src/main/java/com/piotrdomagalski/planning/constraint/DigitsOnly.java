package com.piotrdomagalski.planning.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Annotation used for Spring validation, ensures that provided string contains digits only
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DigitsOnlyValidator.class)
public @interface DigitsOnly {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

