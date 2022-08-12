package com.piotrdomagalski.planning.truck_driver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class TruckDriverControllerUpdateArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
//                new TruckDriverNewUpdateDTO("Test Driver", "111-111-111", "ID123456");
                //update dto
                //update json
                //result dto
                Arguments.of(
                        new TruckDriverNewUpdateDTO("New Name", null, null),
                        """
                                {
                                  "fullName": "New Name"
                                }
                                """,
                        new TruckDriverNewUpdateDTO("New Name", "111-111-111", "ID123456")
                ),
                Arguments.of(
                        new TruckDriverNewUpdateDTO(null, "555555555", null),
                        """
                                {
                                  "tel": "555555555"
                                }
                                """,
                        new TruckDriverNewUpdateDTO("Test Driver", "555-555-555", "ID123456")
                ),
                Arguments.of(
                        new TruckDriverNewUpdateDTO(null, null, "ABC654321"),
                        """
                                {
                                  "idDocument": "ABC654321"
                                }
                                """,
                        new TruckDriverNewUpdateDTO("Test Driver", "111-111-111", "ABC654321")
                ),
                Arguments.of(
                        new TruckDriverNewUpdateDTO("New Name", "555555555", "ABC654321"),
                        """
                                {
                                  "fullName": "New Name",
                                  "idDocument": "ABC654321",
                                  "tel": "555555555"
                                }
                                """,
                        new TruckDriverNewUpdateDTO("New Name", "555-555-555", "ABC654321")
                )
        );
    }
}
