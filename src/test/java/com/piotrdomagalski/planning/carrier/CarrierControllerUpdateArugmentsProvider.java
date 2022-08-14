package com.piotrdomagalski.planning.carrier;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class CarrierControllerUpdateArugmentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
//                new CarrierNewUpdateDTO("123456", "Test Carrier", "Testland", 1.2)
                //update dto
                //update json
                //result dto
                Arguments.of(
                        new CarrierNewUpdateDTO("654987", null, null, null),
                        """
                                {
                                  "sap": "654987"
                                }
                                """,
                        new CarrierNewUpdateDTO("654987", "Test Carrier", "Testland", 1.2)
                ),
                Arguments.of(
                        new CarrierNewUpdateDTO(null, "New Name", null, null),
                        """
                                {
                                "name": "New Name"
                                }
                                """,
                        new CarrierNewUpdateDTO("123456", "New Name", "Testland", 1.2)
                ),
                Arguments.of(
                        new CarrierNewUpdateDTO(null, null, "New Origin", null),
                        """
                                {
                                "origin": "New Origin"
                                }
                                """,
                        new CarrierNewUpdateDTO("123456", "Test Carrier", "New Origin", 1.2)
                ),
                Arguments.of(
                        new CarrierNewUpdateDTO(null, null, null, 1.5),
                        """
                                {
                                  "rate": 1.5
                                }
                                """,
                        new CarrierNewUpdateDTO("123456", "Test Carrier", "Testland", 1.5)
                ),
                Arguments.of(
                        new CarrierNewUpdateDTO("654987", "New Name", "New Origin", 1.5),
                        """
                                {
                                  "name": "New Name",
                                  "origin": "New Origin",
                                  "rate": 1.5,
                                  "sap": "654987"
                                }
                                """,
                        new CarrierNewUpdateDTO("654987", "New Name", "New Origin", 1.5)
                )
        );
    }
}
