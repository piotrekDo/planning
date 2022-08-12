package com.piotrdomagalski.planning.truck_driver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class TruckDriverControllerUpdateNotValidArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //json
                //error code
                //error name
                //error details for name
                //error details for tel
                //error details for idDocument
                Arguments.of(
                        """
                                {
                                  "fullName": "A"
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Driver's name must be between 3 anc 100 characters"},
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "fullName": "Al"
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Driver's name must be between 3 anc 100 characters"},
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                    "tel": "string"
                                }
                                """,
                        400,
                        "Bad Request",
                        null,
                        new String[]{"Driver's tel can be digits only", "Driver's tel should be 9 digits, NO SEPARATORS"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                    "tel": "555-555-555"
                                }
                                """,
                        400,
                        "Bad Request",
                        null,
                        new String[]{"Driver's tel can be digits only", "Driver's tel should be 9 digits, NO SEPARATORS"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                    "tel": "12345678"
                                }
                                """,
                        400,
                        "Bad Request",
                        null,
                        new String[]{"Driver's tel should be 9 digits, NO SEPARATORS"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                    "tel": "1234567890"
                                }
                                """,
                        400,
                        "Bad Request",
                        null,
                        new String[]{"Driver's tel should be 9 digits, NO SEPARATORS"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                    "idDocument": "string"
                                }
                                """,
                        400,
                        "Bad Request",
                        null,
                        null,
                        new String[]{"Driver's id should be 8 characters if passport, 9 if ID document, NO SEPARATORS"}
                )
        );
    }
}
