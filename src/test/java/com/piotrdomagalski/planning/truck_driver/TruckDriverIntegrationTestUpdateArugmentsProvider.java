package com.piotrdomagalski.planning.truck_driver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class TruckDriverIntegrationTestUpdateArugmentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                // TruckDriverEntity.newTruckDriver("Test Driver", "111-111-111", "ID123456");
                //update json
                //result dto
                //result entity
                Arguments.of(
                        """
                                {
                                  "fullName": "New Name"
                                }
                                """,
                        new TruckDriverNewUpdateDTO("New Name", "111-111-111", "ID123456"),
                        new TruckDriverEntity(1L, "New Name", "111-111-111", "ID123456", null, null)
                ),
                Arguments.of(
                        """
                                {
                                  "tel": "555555555"
                                }
                                """,
                        new TruckDriverNewUpdateDTO("Test Driver", "555-555-555", "ID123456"),
                        new TruckDriverEntity(1L, "Test Driver", "555-555-555", "ID123456", null, null)
                ),
                Arguments.of(
                        """
                                {
                                  "idDocument": "ABC654321"
                                }
                                """,
                        new TruckDriverNewUpdateDTO("Test Driver", "111-111-111", "ABC654321"),
                        new TruckDriverEntity(1L, "Test Driver", "111-111-111", "ABC654321", null, null)
                ),
                Arguments.of(
                        """
                                {
                                  "fullName": "New Name",
                                  "idDocument": "ABC654321",
                                  "tel": "555555555"
                                }
                                """,
                        new TruckDriverNewUpdateDTO("New Name", "555-555-555", "ABC654321"),
                        new TruckDriverEntity(1L, "New Name", "555-555-555", "ABC654321", null, null)
                )
        );
    }
}
