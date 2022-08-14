package com.piotrdomagalski.planning.tautliner;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDateTime;
import java.util.stream.Stream;

class TautlinerIntegrationTestUpdateArugmentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //TautlinerEntity.newTautliner(true, "TAUT1234", LocalDateTime.of(2023, 10, 10, 0, 0, 0));
                //update json
                //result dto
                //result entity
                Arguments.of(
                        """
                                {
                                  "xpo": true
                                }
                                """,
                        new TautlinerNewUpdateDTO(true, "TAUT1234", "2023-10-10T00:00"),
                        new TautlinerEntity(1L, true, "TAUT1234", LocalDateTime.of(2023, 10, 10, 0, 0, 0), null, null)
                ),
                Arguments.of(
                        """
                                {
                                  "xpo": false
                                }
                                """,
                        new TautlinerNewUpdateDTO(false, "TAUT1234", "2023-10-10T00:00"),
                        new TautlinerEntity(1L, false, "TAUT1234", LocalDateTime.of(2023, 10, 10, 0, 0, 0), null, null)
                ),
                Arguments.of(
                        """
                                {
                                  "tautlinerPlates": "TEST123"
                                }
                                """,
                        new TautlinerNewUpdateDTO(true, "TEST123", "2023-10-10T00:00"),
                        new TautlinerEntity(1L, true, "TEST123", LocalDateTime.of(2023, 10, 10, 0, 0, 0), null, null)
                ),
                Arguments.of(
                        """
                                {
                                  "techInspection": "2022-01-01"
                                }
                                """,
                        new TautlinerNewUpdateDTO(true, "TAUT1234", "2022-01-01T00:00"),
                        new TautlinerEntity(1L, true, "TAUT1234", LocalDateTime.of(2022, 01, 01, 0, 0, 0), null, null)
                ),
                Arguments.of(
                        """
                                {
                                  "tautlinerPlates": "TEST123",
                                  "techInspection": "2022-01-01",
                                  "xpo": false
                                }
                                """,
                        new TautlinerNewUpdateDTO(false, "TEST123", "2022-01-01T00:00"),
                        new TautlinerEntity(1L, false, "TEST123", LocalDateTime.of(2022, 01, 01, 0, 0, 0), null, null)
                )
        );
    }
}
