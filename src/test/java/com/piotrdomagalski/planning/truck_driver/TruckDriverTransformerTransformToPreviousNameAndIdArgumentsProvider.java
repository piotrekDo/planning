package com.piotrdomagalski.planning.truck_driver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class TruckDriverTransformerTransformToPreviousNameAndIdArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                // originalName is always equals to "Test Name"
                // originalId is always equals to "ID123456"
                // nameChanged
                //idDocumentChanged
                // expected result
                Arguments.of(
                        "New Name",
                        "ID000000",
                        "New Name ID000000"
                ),
                Arguments.of(
                        "New Name",
                        null,
                        "New Name ID123456"
                ),
                Arguments.of(
                        null,
                        "ID000000",
                        "Test Name ID000000"
                ),
                Arguments.of(
                        null,
                        null,
                        null
                )
        );
    }
}
