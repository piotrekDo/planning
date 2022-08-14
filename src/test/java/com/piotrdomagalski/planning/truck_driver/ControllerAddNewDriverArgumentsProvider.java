package com.piotrdomagalski.planning.truck_driver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class ControllerAddNewDriverArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(
                        """
                                {
                                    "fullName": "Te",
                                    "idDocument": "ID123456",
                                    "tel": "999000888"
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
                                    "fullName": " ",
                                    "idDocument": "ID123456",
                                    "tel": "999000888"
                                }
                                        """,
                        400,
                        "Bad Request",
                        new String[]{"Driver's name must be between 3 anc 100 characters", "Driver's name cannot be blank!"},
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                    "fullName": "Test Driver",
                                    "idDocument": "ID123456",
                                    "tel": "9990008"
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
                                    "fullName": "Test Driver",
                                    "idDocument": "ID123456",
                                    "tel": "999-000-888"
                                }
                                        """,
                        400,
                        "Bad Request",
                        null,
                        new String[]{"Driver's tel should be 9 digits, NO SEPARATORS", "Driver's tel can be digits only"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                    "fullName": "Test Driver",
                                    "idDocument": "ID123456",
                                    "tel": "99440004888"
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
                                    "fullName": "Test Driver",
                                    "idDocument": "ID123456",
                                    "tel": "hjkl"
                                }
                                        """,
                        400,
                        "Bad Request",
                        null,
                        new String[]{"Driver's tel should be 9 digits, NO SEPARATORS", "Driver's tel can be digits only"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                    "fullName": "Test Driver",
                                    "idDocument": "ID12345699",
                                    "tel": "999000888"
                                }
                                        """,
                        400,
                        "Bad Request",
                        null,
                        null,
                        new String[]{"Driver's id should be 8 characters if passport, 9 if ID document, NO SEPARATORS"}
                ),
                Arguments.of(
                        """
                                {
                                    "fullName": "Test Driver",
                                    "idDocument": "12345699",
                                    "tel": "999000888"
                                }
                                        """,
                        400,
                        "Bad Request",
                        null,
                        null,
                        new String[]{"ID document has to start with letters, no separators"}
                ),
                Arguments.of(
                        """
                                {
                                    "fullName": "Test Driver",
                                    "idDocument": "ID-123456",
                                    "tel": "999000888"
                                }
                                        """,
                        400,
                        "Bad Request",
                        null,
                        null,
                        new String[]{"ID document has to start with letters, no separators"}
                ),
                Arguments.of(
                        """
                                {
                                    "fullName": "Te",
                                    "idDocument": "321312123456",
                                    "tel": "999/000888 98"
                                }
                                        """,
                        400,
                        "Bad Request",
                        new String[]{"Driver's name must be between 3 anc 100 characters"},
                        new String[]{"Driver's tel can be digits only", "Driver's tel should be 9 digits, NO SEPARATORS"},
                        new String[]{"Driver's id should be 8 characters if passport, 9 if ID document, NO SEPARATORS", "ID document has to start with letters, no separators"}
                )
        );
    }
}
