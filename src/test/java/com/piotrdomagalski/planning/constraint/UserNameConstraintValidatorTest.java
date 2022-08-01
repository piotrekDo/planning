package com.piotrdomagalski.planning.constraint;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserNameConstraintValidatorTest {

    private final UserNameConstraintValidator validator = new UserNameConstraintValidator();

    @ParameterizedTest
    @ValueSource(strings = {"admin", "User", "AdmiN", "User1", "User_admin11"})
    void validator_should_return_true_for_correct_value(String input){
        assertTrue(isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1admin", "User 1", "_lol", "lol_", "user__user", "&user", "%&^", "user+"})
    void validator_should_return_false_for_incorrect_value(String input){
        assertFalse(isValid(input));
    }

    private boolean isValid(String value) {
        return validator.isValid(value, null);
    }
}