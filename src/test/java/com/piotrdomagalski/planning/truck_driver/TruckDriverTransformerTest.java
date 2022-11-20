package com.piotrdomagalski.planning.truck_driver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TruckDriverTransformerTest {

    TruckDriverTransformer transformer = new TruckDriverTransformer();

    @ParameterizedTest
    @ArgumentsSource(TruckDriverTransformerTransformToPreviousNameAndIdArgumentsProvider.class)
    void should_return_correct_previous_name_and_id(String nameChanged, String idDocumentChanged, String expectedResult) {
        //given
        String originalName = "Test Name", originalId = "ID123456";

        //when
        String result = transformer.transformToPreviousNameAndId(nameChanged, idDocumentChanged, originalName, originalId);

        //then
        Assertions.assertEquals(expectedResult, result);
    }

}