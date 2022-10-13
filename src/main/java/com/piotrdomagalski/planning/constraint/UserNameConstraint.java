package com.piotrdomagalski.planning.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Annotation for Spring validation. Ensures that prided username.
 * Username must start with 3 letters. Can only contain letters, digits and underscore.
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UserNameConstraintValidator.class)
public @interface UserNameConstraint {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
