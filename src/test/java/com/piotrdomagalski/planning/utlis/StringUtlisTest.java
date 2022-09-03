package com.piotrdomagalski.planning.utlis;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class StringUtlisTest {

    @ParameterizedTest
    @ArgumentsSource(MyStringUtlisCapitalizeArgumentsProvider.class)
    void capitalize_test(String input, String expected) {
        assertEquals(expected, StringUtlis.capitalize(input));
    }

}