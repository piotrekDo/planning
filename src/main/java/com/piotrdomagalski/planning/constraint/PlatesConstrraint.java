package com.piotrdomagalski.planning.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Annotation used for Spring validation, ensures that provided string acts as a truck/ tautliner plates.
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PlatesConstrraintValidator.class)

public @interface PlatesConstrraint {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
