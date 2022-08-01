package com.piotrdomagalski.planning.tautliner;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDateTime;
import java.util.stream.Stream;

class TautlinerUpdateTautlinerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //plates to find
                //tautliner by plates
                //tautliner provided to update method
                //tautliner provided, translated to entity
                //expected result
                Arguments.of(
                        "ABCD1234",
                        new TautlinerEntity(1L, true, "ABCD1234",
                                LocalDateTime.of(2022, 10, 10, 0, 0, 0),
                                null, null),
                        new TautlinerNewUpdateDTO(false, null, null),
                        new TautlinerEntity(false, null, null, null, null),
                        new TautlinerNewUpdateDTO(false, "ABCD1234", "10-10-2022")
                ),
                Arguments.of(
                        "ABCD1234",
                        new TautlinerEntity(1L, true, "ABCD1234",
                                LocalDateTime.of(2022, 10, 10, 0, 0, 0),
                                null, null),
                        new TautlinerNewUpdateDTO(null, "NEW12334", null),
                        new TautlinerEntity(null, "NEW12334", null, null, null),
                        new TautlinerNewUpdateDTO(true, "NEW12334", "10-10-2022")
                ),
                Arguments.of(
                        "ABCD1234",
                        new TautlinerEntity(1L, true, "ABCD1234",
                                LocalDateTime.of(2022, 10, 10, 0, 0, 0),
                                null, null),
                        new TautlinerNewUpdateDTO(null, null, "12-10-2021"),
                        new TautlinerEntity(null, null, LocalDateTime.of(2021, 10, 12, 0, 0, 0), null, null),
                        new TautlinerNewUpdateDTO(true, "ABCD1234", "12-10-2021")
                ),
                Arguments.of(
                        "ABCD1234",
                        new TautlinerEntity(1L, true, "ABCD1234",
                                LocalDateTime.of(2022, 10, 10, 0, 0, 0),
                                null, null),
                        new TautlinerNewUpdateDTO(false, "NEW1234", "12-10-2021"),
                        new TautlinerEntity(false, "NEW1234", LocalDateTime.of(2021, 10, 12, 0, 0, 0), null, null),
                        new TautlinerNewUpdateDTO(false, "NEW1234", "12-10-2021")
                ),
                Arguments.of(
                        "ABCD1234",
                        new TautlinerEntity(1L, true, "ABCD1234",
                                LocalDateTime.of(2022, 10, 10, 0, 0, 0),
                                null, null),
                        new TautlinerNewUpdateDTO(false, "NEW1234", null),
                        new TautlinerEntity(false, "NEW1234", null, null, null),
                        new TautlinerNewUpdateDTO(false, "NEW1234", "10-10-2022")
                )
        );
    }
}
