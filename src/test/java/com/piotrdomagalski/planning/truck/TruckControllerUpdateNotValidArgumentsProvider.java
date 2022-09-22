package com.piotrdomagalski.planning.truck;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class TruckControllerUpdateNotValidArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //json
                //error code
                //error name
                //error details for each field
                Arguments.of(
                        """
                                {
                                "truckPlates": "AB"
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Truck's plates must be between 3 and 15 characters"}
                ),
                Arguments.of(
                        """
                                {
                                "truckPlates": "12"
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Truck's plates must be between 3 and 15 characters", "Trucks's plates must start with letter, no special characters, eg. PO23211"}
                ),
                Arguments.of(
                        """
                                {
                                "truckPlates": "1A12345"
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Trucks's plates must start with letter, no special characters, eg. PO23211"}
                ),
                Arguments.of(
                        """
                                {
                                "truckPlates": "ABC123456789534543"
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Truck's plates must be between 3 and 15 characters"}
                )
        );
    }
}
