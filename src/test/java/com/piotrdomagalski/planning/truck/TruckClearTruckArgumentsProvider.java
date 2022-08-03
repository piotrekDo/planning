package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Stream;

public class TruckClearTruckArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //truck to clean
                //truck's carrier
                //truck's driver
                //truck's tautliner
                Arguments.of(
                        new TruckEntity(2L, "TRUCK123", true, null, null, null),
                        new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2,
                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        new TruckDriverEntity(3L, "Test name", "555555555", "ID123456", null, null),
                        new TautlinerEntity(1L, true, "TAUT123", LocalDateTime.now(), null, null)
                )
        );
    }
}
