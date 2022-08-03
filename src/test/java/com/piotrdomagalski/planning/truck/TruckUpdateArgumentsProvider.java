package com.piotrdomagalski.planning.truck;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class TruckUpdateArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //plates of truck's to update
                //data to update
                //truck found by plates
                //data translated from dto to entity
                //saved entity
                Arguments.of(
                        "EXIST1234",
                        new TruckNewUpdateDTO("LPO9078", null),
                        new TruckEntity(13L, "EXIST1234", true, null, null, null),
                        new TruckEntity(null, "LPO9078", null, null, null, null),
                        new TruckEntity(13L, "LPO9078", true, null, null, null)
                ),
                Arguments.of(
                        "EXIST1234",
                        new TruckNewUpdateDTO(null, false),
                        new TruckEntity(13L, "EXIST1234", true, null, null, null),
                        new TruckEntity(null, null, false, null, null, null),
                        new TruckEntity(13L, "EXIST1234", false, null, null, null)
                ),
                Arguments.of(
                        "EXIST1234",
                        new TruckNewUpdateDTO("LPO9078", false),
                        new TruckEntity(13L, "EXIST1234", true, null, null, null),
                        new TruckEntity(null, "LPO9078", false, null, null, null),
                        new TruckEntity(13L, "LPO9078", false, null, null, null)
                )
        );
    }
}
