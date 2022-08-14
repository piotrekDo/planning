package com.piotrdomagalski.planning.truck_driver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.piotrdomagalski.planning.truck_driver.TruckDriverDTOdataParser.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
class TruckDriverDTOdataParserTest {


    @Test
    void fullNameParser_test() {
        //given
        String input = " aNDrzeJ   kOWALski ";
        String expected = "Andrzej Kowalski";

        //when
        String result = fullNameParser(input);

        //then
        assertEquals(expected, result);
    }

    @Test
    void telParser_test() {
        //given
        String input = "888999000";
        String expected = "888-999-000";

        //when
        String result = telParser(input);

        //then
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource(value = {"ID123456;ID123456", "ide123456;IDE123456", "iD676767;ID676767"}, delimiter = ';')
    void idDocument_Test(String input, String expected) {
        assertEquals(expected, idDocumentParser(input));
    }

}