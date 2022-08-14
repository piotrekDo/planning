package com.piotrdomagalski.planning.carrier;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.stream.Stream;

public class CarrierUpdateArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //sap of carrier to update
                //dto to update
                //carrier found by sap
                //dto translated to entity
                //saved entity
                Arguments.of(
                        "456456",
                        new CarrierNewUpdateDTO("111222", null, null, null),
                        new CarrierEntity(465L, "456456", "Test carrier", "Carrierland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        new CarrierEntity(null, "111222", null, null, null, null, null, null),
                        new CarrierEntity(465L, "111222", "Test carrier", "Carrierland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                ),
                Arguments.of(
                        "456456",
                        new CarrierNewUpdateDTO(null, "New name", null, null),
                        new CarrierEntity(465L, "456456", "Test carrier", "Carrierland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        new CarrierEntity(null, null, "New name", null, null, null, null, null),
                        new CarrierEntity(465L, "456456", "New name", "Carrierland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                ),
                Arguments.of(
                        "456456",
                        new CarrierNewUpdateDTO(null, null, "New origin", null),
                        new CarrierEntity(465L, "456456", "Test carrier", "Carrierland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        new CarrierEntity(null, null, null, "New origin", null, null, null, null),
                        new CarrierEntity(465L, "111222", "Test carrier", "New origin", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                ),
                Arguments.of(
                        "456456",
                        new CarrierNewUpdateDTO(null, null, null, 1.5),
                        new CarrierEntity(465L, "456456", "Test carrier", "Carrierland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        new CarrierEntity(null, null, null, null, 1.5, null, null, null),
                        new CarrierEntity(465L, "111222", "Test carrier", "Carrierland", 1.5, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                ),
                Arguments.of(
                        "456456",
                        new CarrierNewUpdateDTO("111222", "New name", "New Origin", 1.5),
                        new CarrierEntity(465L, "456456", "Test carrier", "Carrierland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        new CarrierEntity(null, "111222", "New name", "New Origin", 1.5, null, null, null),
                        new CarrierEntity(465L, "111222", "New name", "New Origin", 1.5, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                )
        );
    }
}
