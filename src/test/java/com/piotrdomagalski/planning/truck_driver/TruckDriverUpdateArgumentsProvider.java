package com.piotrdomagalski.planning.truck_driver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class TruckDriverUpdateArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //if to find
                //driver by id
                //driver provided to update method
                //driver provided, translated to entity
                //expected result
                Arguments.of(
                        90L,
                        new TruckDriverEntity(90L, "Blasphemy Crackerdong", "999666111", "ID123456", null, null),
                        new TruckDriverNewUpdateDTO("New name", null, null),
                        new TruckDriverEntity(null, "New name", null, null, null, null),
                        new TruckDriverNewUpdateDTO("New name", "999666111", "ID123456")
                        ),
                Arguments.of(
                        90L,
                        new TruckDriverEntity(90L, "Blasphemy Crackerdong", "999666111", "ID123456", null, null),
                        new TruckDriverNewUpdateDTO(null, "789987666", null),
                        new TruckDriverEntity(null, null, "789987666", null, null, null),
                        new TruckDriverNewUpdateDTO("Blasphemy Crackerdong", "789987666", "ID123456")
                ),
                Arguments.of(
                        90L,
                        new TruckDriverEntity(90L, "Blasphemy Crackerdong", "999666111", "ID123456", null, null),
                        new TruckDriverNewUpdateDTO(null, null, "ID654321"),
                        new TruckDriverEntity(null, null, null, "ID654321", null, null),
                        new TruckDriverNewUpdateDTO("Blasphemy Crackerdong", "999666111", "ID654321")
                ),
                Arguments.of(
                        90L,
                        new TruckDriverEntity(90L, "Blasphemy Crackerdong", "999666111", "ID123456", null, null),
                        new TruckDriverNewUpdateDTO("New name", "432234567", "ID654322"),
                        new TruckDriverEntity(null, "New name", "432234567", "ID654322", null, null),
                        new TruckDriverNewUpdateDTO("New name", "432234567", "ID654322")
                ),
                Arguments.of(
                        90L,
                        new TruckDriverEntity(90L, "Blasphemy Crackerdong", "999666111", "ID123456", null, null),
                        new TruckDriverNewUpdateDTO("New name", "432234567", null),
                        new TruckDriverEntity(null, "New name", "432234567", null, null, null),
                        new TruckDriverNewUpdateDTO("New name", "432234567", "ID123456")
                )
        );
    }
}
