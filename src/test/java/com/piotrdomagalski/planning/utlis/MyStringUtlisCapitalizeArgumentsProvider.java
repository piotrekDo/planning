package com.piotrdomagalski.planning.utlis;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class MyStringUtlisCapitalizeArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(
                        "andrzej kowalski",
                        "Andrzej Kowalski"
                ),
                Arguments.of(
                        "ANDRZEJ kowALSki",
                        "Andrzej Kowalski"
                ),
                Arguments.of(
                        "ANDRZEJ KOWALSKI",
                        "Andrzej Kowalski"
                ),
                Arguments.of(
                        "Andrzej Kowalski",
                        "Andrzej Kowalski"
                ),
                Arguments.of(
                        "aNDRZEJ kOWALSKI",
                        "Andrzej Kowalski"
                )
        );
    }
}
