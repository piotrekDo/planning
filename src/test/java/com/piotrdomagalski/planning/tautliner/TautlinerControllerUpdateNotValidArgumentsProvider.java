package com.piotrdomagalski.planning.tautliner;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class TautlinerControllerUpdateNotValidArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //json
                //error code
                //error name
                //error details
                Arguments.of(
                        """
                                {
                                  "tautlinerPlates": "11"
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Tautliner's plates must be between 3 and 15 characters", "Tautliner's plates must start with 2-3 letters, eg. PO23211"}
                ),
                Arguments.of(
                        """
                                {
                                  "tautlinerPlates": "AA"
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Tautliner's plates must be between 3 and 15 characters"}
                ),
                Arguments.of(
                        """
                                {
                                  "tautlinerPlates": "123AS4"
                                }
                                """,
                        400,
                        "Bad Request",
                        new String[]{"Tautliner's plates must start with 2-3 letters, eg. PO23211"}
                )
        );
    }
}
