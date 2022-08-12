package com.piotrdomagalski.planning.carrier;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ControllerUpdateCarrierArgumentsProvider implements ArgumentsProvider {
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
                                  "sap": "string"
                                }    
                                """,
                        400,
                        "Bad Request",
                        new String[]{"SAP must be numberic!"},
                        null,
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "sap": "abcd"
                                }    
                                """,
                        400,
                        "Bad Request",
                        new String[]{"SAP must be 6 chracters long", "SAP must be numberic!"},
                        null,
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "sap": "a12345"
                                }    
                                """,
                        400,
                        "Bad Request",
                        new String[]{"SAP must be numberic!"},
                        null,
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "sap": " 123456"
                                }    
                                """,
                        400,
                        "Bad Request",
                        new String[]{"SAP must be 6 chracters long", "SAP must be numberic!"},
                        null,
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "sap": "",
                                  "name": "string"
                                }    
                                """,
                        400,
                        "Bad Request",
                        new String[]{"SAP must be 6 chracters long", "SAP must be numberic!"},
                        null,
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "sap": "1234567"
                                }    
                                """,
                        400,
                        "Bad Request",
                        new String[]{"SAP must be 6 chracters long"},
                        null,
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "sap": "12345"
                                }    
                                """,
                        400,
                        "Bad Request",
                        new String[]{"SAP must be 6 chracters long"},
                        null,
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "name": "no"
                                }    
                                """,
                        400,
                        "Bad Request",
                        null,
                        new String[]{"Name must be between 3 anc 100 characters"},
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "origin": "ds"
                                }    
                                """,
                        400,
                        "Bad Request",
                        null,
                        null,
                        new String[]{"Origin must be between 3 anc 100 characters"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "rate": -1
                                }    
                                """,
                        400,
                        "Bad Request",
                        null,
                        null,
                        null,
                        new String[]{"Rate cannot be negative!"}
                ),
                Arguments.of(
                        """
                                {
                                  "rate": 0
                                }    
                                """,
                        400,
                        "Bad Request",
                        null,
                        null,
                        null,
                        new String[]{"Rate cannot be negative!"}
                )
        );
    }
}
