package com.piotrdomagalski.planning.truck;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class ControllerAddNewTruckArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(
                        """
                                {
                                  "truckPlates": "",
                                  "mega": true
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Truck's plates must be between 3 and 15 characters", "Trucks's plates must start with letter, no special characters, eg. PO23211"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "truckPlates": "AB",
                                  "mega": true
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Truck's plates must be between 3 and 15 characters"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "truckPlates": "789009",
                                  "mega": true
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Trucks's plates must start with letter, no special characters, eg. PO23211"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "truckPlates": "3A27313BB",
                                  "mega": true
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Trucks's plates must start with letter, no special characters, eg. PO23211"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "truckPlates": "1234567890123456",
                                  "mega": true
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Truck's plates must be between 3 and 15 characters", "Trucks's plates must start with letter, no special characters, eg. PO23211"},
                        null
                )
        );
    }
}
