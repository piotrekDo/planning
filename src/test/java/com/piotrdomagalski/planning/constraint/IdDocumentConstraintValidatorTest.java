package com.piotrdomagalski.planning.constraint;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class IdDocumentConstraintValidatorTest {

    private final IdDocumentConstraintValidator validator = new IdDocumentConstraintValidator();

    @ParameterizedTest
    @ValueSource(strings = {"ID123456", "IDE23456", "IDE2345V"})
    void validator_should_return_true_for_correct_values(String input) {
        assertTrue(isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"I123456", "I1234567", "12345678", "1W2E3E4E", "1234567", "ID 5678", " ID123456", "IDE 5679"})
    void validator_should_return_false_for_incorrect_values(String input) {
        assertFalse(isValid(input));
    }

    private boolean isValid(String value) {
        return validator.isValid(value, null);
    }

}