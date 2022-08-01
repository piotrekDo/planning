package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Stream;

class TautlinerClearTautlinerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //tautliner to clean
                //tauliner's truck
                //tautliner's carrier
                Arguments.of(
                        new TautlinerEntity(1L, true, "TAUT123", LocalDateTime.now(), null, null),
                        new TruckEntity(2L, "TRUCK123", true, null, null, null),
                        new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2,
                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                )
        );
    }
}
