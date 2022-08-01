package com.piotrdomagalski.planning.carrier;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class ControllerAddNewCarrierArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(
                        """
                                {
                                  "sap": "string",
                                  "name": "string",
                                  "origin": "string",
                                  "rate": 1
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
                                  "sap": "abcd",
                                  "name": "string",
                                  "origin": "string",
                                  "rate": 1
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
                                  "sap": "a12345",
                                  "name": "string",
                                  "origin": "string",
                                  "rate": 1
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
                                  "sap": " 123456",
                                  "name": "string",
                                  "origin": "string",
                                  "rate": 1
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
                                  "name": "string",
                                  "origin": "string",
                                  "rate": 1
                                }    
                                """,
                        400,
                        "Bad Request",
                        new String[]{"SAP must be 6 chracters long", "SAP must be numberic!", "Carrier SAP cannot be blank!"},
                        null,
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "sap": "1234567",
                                  "name": "string",
                                  "origin": "string",
                                  "rate": 1
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
                                  "sap": "12345",
                                  "name": "string",
                                  "origin": "string",
                                  "rate": 1
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
                                  "sap": "123456",
                                  "name": "",
                                  "origin": "string",
                                  "rate": 1
                                }    
                                """,
                        400,
                        "Bad Request",
                        null,
                        new String[]{"Name cannot be blank!", "Name must be between 3 anc 100 characters"},
                        null,
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "sap": "123456",
                                  "name": "ds",
                                  "origin": "string",
                                  "rate": 1
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
                                  "sap": "123456",
                                  "name": "string",
                                  "origin": "",
                                  "rate": 1
                                }    
                                """,
                        400,
                        "Bad Request",
                        null,
                        null,
                        new String[]{"Origin cannot be blank!", "Origin must be between 3 anc 100 characters"},
                        null
                ),
                Arguments.of(
                        """
                                {
                                  "sap": "123456",
                                  "name": "string",
                                  "origin": "ds",
                                  "rate": 1
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
                                  "sap": "string",
                                  "name": "string",
                                  "origin": "string",
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
                                  "sap": "string",
                                  "name": "string",
                                  "origin": "string",
                                  "rate": 0
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
                                  "sap": "",
                                  "name": "",
                                  "origin": "",
                                  "rate": 0
                                }    
                                """,
                        400,
                        "Bad Request",
                        new String[]{"SAP must be 6 chracters long", "SAP must be numberic!", "Carrier SAP cannot be blank!"},
                        new String[]{"Name cannot be blank!", "Name must be between 3 anc 100 characters"},
                        new String[]{"Origin cannot be blank!", "Origin must be between 3 anc 100 characters"},
                        new String[]{"Rate cannot be negative!"}
                )
        );
    }
}
