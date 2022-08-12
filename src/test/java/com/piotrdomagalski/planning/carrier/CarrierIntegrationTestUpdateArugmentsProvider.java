package com.piotrdomagalski.planning.carrier;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.stream.Stream;

public class CarrierIntegrationTestUpdateArugmentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
//                CarrierEntity.newCarrier("123456", "Test Carrier", "Testland", 1.2);
                //update json
                //result dto
                //result entity
                Arguments.of(
                        """
                                {
                                  "sap": "654987"
                                }
                                """,
                        new CarrierNewUpdateDTO("654987", "Test Carrier", "Testland", 1.2),
                        new CarrierEntity(1L, "654987", "Test Carrier", "Testland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                ),
                Arguments.of(
                        """
                                {
                                "name": "New Name"
                                }
                                """,
                        new CarrierNewUpdateDTO("123456", "New Name", "Testland", 1.2),
                        new CarrierEntity(1L, "123456", "New Name", "Testland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                ),
                Arguments.of(
                        """
                                {
                                "origin": "New Origin"
                                }
                                """,
                        new CarrierNewUpdateDTO("123456", "Test Carrier", "New Origin", 1.2),
                        new CarrierEntity(1L, "123456", "Test Carrier", "New Origin", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                ),
                Arguments.of(
                        """
                                {
                                  "rate": 1.5
                                }
                                """,
                        new CarrierNewUpdateDTO("123456", "Test Carrier", "Testland", 1.5),
                        new CarrierEntity(1L, "123456", "Test Carrier", "Testland", 1.5, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                ),
                Arguments.of(
                        """
                                {
                                  "name": "New Name",
                                  "origin": "New Origin",
                                  "rate": 1.5,
                                  "sap": "654987"
                                }
                                """,
                        new CarrierNewUpdateDTO("654987", "New Name", "New Origin", 1.5),
                        new CarrierEntity(1L, "654987", "New Name", "New Origin", 1.5, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                )
        );
    }
}
