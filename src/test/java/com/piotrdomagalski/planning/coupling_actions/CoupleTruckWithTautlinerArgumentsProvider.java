package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class CoupleTruckWithTautlinerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //carrier
                //truck
                //tautliner
                Arguments.of(
                        new CarrierEntity(1L, "123456", "Test Carrier", "Testowo", 1.1, null, null, null),
                        new TruckEntity(12L, "TEST1234", true, null, null, null),
                        new TautlinerEntity(9L, false, "ABCD1234", null, null, null)
                ),
                Arguments.of(
                        new CarrierEntity(1L, "123456", "Test Carrier", "Testowo", 1.1, null, null, null),
                        new TruckEntity(12L, "TEST1234", true, null, null, null),
                        new TautlinerEntity(9L, true, "ABCD1234", null, null, null)
                )
        );
    }
}
