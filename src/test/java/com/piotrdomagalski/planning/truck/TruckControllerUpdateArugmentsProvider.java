package com.piotrdomagalski.planning.truck;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class TruckControllerUpdateArugmentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //plates
                //update dto
                //update json
                //result dto
                Arguments.of(
                        "TRUCK1234",
                        new TruckNewUpdateDTO("TEST124", null),
                        """
                                {
                                "truckPlates": "TEST124"
                                }                        
                                """,
                        new TruckNewUpdateDTO("TEST124", true)
                ),
                Arguments.of(
                        "TRUCK1234",
                        new TruckNewUpdateDTO(null, true),
                        """
                                {
                                "mega": true
                                }                        
                                """,
                        new TruckNewUpdateDTO("TRUCK1234", true)
                ),
                Arguments.of(
                        "TRUCK1234",
                        new TruckNewUpdateDTO(null, false),
                        """
                                {
                                "mega": false
                                }                        
                                """,
                        new TruckNewUpdateDTO("TRUCK1234", false)
                ),
                Arguments.of(
                        "TRUCK1234",
                        new TruckNewUpdateDTO("TRUCK1234", true),
                        """
                                {
                                "truckPlates": "TRUCK1234",
                                "mega": true
                                }                        
                                """,
                        new TruckNewUpdateDTO("TEST124", true)
                ),
                Arguments.of(
                        "TRUCK1234",
                        new TruckNewUpdateDTO(null, null),
                        """
                                {
                                }                        
                                """,
                        new TruckNewUpdateDTO(null, null),
                        new TruckNewUpdateDTO("TEST124", true)
                )
        );
    }
}
