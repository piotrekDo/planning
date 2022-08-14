package com.piotrdomagalski.planning.truck;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class TruckIntegrationTestUpdateArugmentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //TruckEntity truck = TruckEntity.newTruck("TRUCK1234", true);
                //update dto
                //update json
                //result dto
                //result entity
                Arguments.of(
                        new TruckNewUpdateDTO("TEST124", null),
                        """
                                {
                                "truckPlates": "TEST124"
                                }                        
                                """,
                        new TruckNewUpdateDTO("TEST124", true),
                        new TruckEntity(1L, "TEST124", true, null, null, null)
                ),
                Arguments.of(
                        new TruckNewUpdateDTO(null, true),
                        """
                                {
                                "mega": true
                                }                        
                                """,
                        new TruckNewUpdateDTO("TRUCK1234", true),
                        new TruckEntity(1L, "TRUCK1234", true, null, null, null)
                ),
                Arguments.of(
                        new TruckNewUpdateDTO(null, false),
                        """
                                {
                                "mega": false
                                }                        
                                """,
                        new TruckNewUpdateDTO("TRUCK1234", false),
                        new TruckEntity(1L, "TRUCK1234", false, null, null, null)
                ),
                Arguments.of(
                        new TruckNewUpdateDTO("TRUCK1234", true),
                        """
                                {
                                "truckPlates": "TRUCK1234",
                                "mega": true
                                }                        
                                """,
                        new TruckNewUpdateDTO("TRUCK1234", true),
                        new TruckEntity(1L, "TRUCK1234", true, null, null, null)
                )
        );
    }
}
