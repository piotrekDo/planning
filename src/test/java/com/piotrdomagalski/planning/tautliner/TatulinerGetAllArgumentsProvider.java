package com.piotrdomagalski.planning.tautliner;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class TatulinerGetAllArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new TautlinerEntity(true, "APL1234", null, null, null),
                                new TautlinerEntity(true, "BAD5256", null, null, null),
                                new TautlinerEntity(true, "DAS1234", null, null, null),
                                new TautlinerEntity(true, "CAL5739", null, null, null),
                                new TautlinerEntity(true, "EAD1234", null, null, null)
                        ),
                        List.of(
                                new TautlinerEntity(1L,true, "APL1234", null, null, null),
                                new TautlinerEntity(2L,true, "BAD5256", null, null, null),
                                new TautlinerEntity(4L,true, "CAL5739", null, null, null),
                                new TautlinerEntity(3L,true, "DAS1234", null, null, null),
                                new TautlinerEntity(5L,true, "EAD1234", null, null, null)
                        )
                ),
                Arguments.of(
                        List.of(
                                new TautlinerEntity(true, "CAL5739", null, null, null),
                                new TautlinerEntity(true, "EAD1234", null, null, null),
                                new TautlinerEntity(true, "DAS1234", null, null, null),
                                new TautlinerEntity(true, "BAD5256", null, null, null),
                                new TautlinerEntity(true, "APL1234", null, null, null)
                                ),
                        List.of(
                                new TautlinerEntity(1L,true, "APL1234", null, null, null),
                                new TautlinerEntity(2L,true, "BAD5256", null, null, null),
                                new TautlinerEntity(4L,true, "CAL5739", null, null, null),
                                new TautlinerEntity(3L,true, "DAS1234", null, null, null),
                                new TautlinerEntity(5L,true, "EAD1234", null, null, null)
                        )
                )
        );
    }
}
