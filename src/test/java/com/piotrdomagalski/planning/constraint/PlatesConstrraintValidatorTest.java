package com.piotrdomagalski.planning.constraint;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class PlatesConstrraintValidatorTest {

    private final PlatesConstrraintValidator validator = new PlatesConstrraintValidator();

    @ParameterizedTest
    @ValueSource(strings = {"SBE123Y", "S2STAR", "AAAYYY", "HK9009I"})
    void validator_should_return_true_for_correct_values(String input) {
        assertTrue(isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "12", "12SBEUI", "SBE-32gd", "S2STAR-"})
    void validator_should_return_false_for_incorrect_values(String input) {
        assertFalse(isValid(input));
    }

    private boolean isValid(String value) {
        return validator.isValid(value, null);
    }

}