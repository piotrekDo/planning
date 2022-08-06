package com.piotrdomagalski.planning.app;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PlatesConstrraintValidator.class)

public @interface PlatesConstrraint {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
