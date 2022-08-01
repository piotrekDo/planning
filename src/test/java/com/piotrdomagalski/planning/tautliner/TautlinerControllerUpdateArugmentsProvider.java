package com.piotrdomagalski.planning.tautliner;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class TautlinerControllerUpdateArugmentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
//                new TautlinerNewUpdateDTO(true, "TAUT1234", "2023-10-10T00:00:00")
                //update dto
                //update json
                //result dto
                Arguments.of(
                        new TautlinerNewUpdateDTO(true, null, null),
                        """
                                {
                                  "xpo": true
                                }
                                """,
                        new TautlinerNewUpdateDTO(true, "TAUT1234", "2023-10-10T00:00:00")
                ),
                Arguments.of(
                        new TautlinerNewUpdateDTO(false, null, null),
                        """
                                {
                                  "xpo": false
                                }
                                """,
                        new TautlinerNewUpdateDTO(false, "TAUT1234", "2023-10-10T00:00:00")
                ),
                Arguments.of(
                        new TautlinerNewUpdateDTO(null, "TEST123", null),
                        """
                                {
                                  "tautlinerPlates": "TEST123"
                                }
                                """,
                        new TautlinerNewUpdateDTO(true, "TEST123", "2023-10-10T00:00:00")
                ),
                Arguments.of(
                        new TautlinerNewUpdateDTO(null, null, "2022-01-01"),
                        """
                                {
                                  "techInspection": "2022-01-01"
                                }
                                """,
                        new TautlinerNewUpdateDTO(true, "TAUT1234", "2022-01-01T00:00:00")
                ),
                Arguments.of(
                        new TautlinerNewUpdateDTO(false, "TEST123", "2022-01-01"),
                        """
                                {
                                  "tautlinerPlates": "TEST123",
                                  "techInspection": "2022-01-01",
                                  "xpo": false
                                }
                                """,
                        new TautlinerNewUpdateDTO(false, "TEST123", "2022-01-01T00:00:00")
                )
        );
    }
}
