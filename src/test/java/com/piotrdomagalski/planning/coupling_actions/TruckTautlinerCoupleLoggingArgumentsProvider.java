package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class TruckTautlinerCoupleLoggingArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                // couple
                // truck found
                // tautliner found
                // current truck's tautliner
                // current tautliner's truck
                // log truck for Mockito verify times
                // log tautliner truck for Mockito verify times
                // log current tautliner truck for Mockito verify times
                // log current truck truck for Mockito verify times
                Arguments.of(
                        new TruckTautlinerCouple("TRUCK123", "TAUT123"),
                        new TruckEntity("TRUCK123", true, null, null, null),
                        new TautlinerEntity(true, "TAUT123", null, null, null),
                        new TautlinerEntity(true, "TAUT123", null, null, null),
                        new TruckEntity("TRUCK123", true, null, null, null),
                        0,
                        0,
                        0,
                        0
                ),
                Arguments.of(
                        new TruckTautlinerCouple("TRUCK123", "TAUT123"),
                        new TruckEntity("TRUCK123", true, null, null, null),
                        new TautlinerEntity(true, "TAUT123", null, null, null),
                        new TautlinerEntity(true, "ANOTHERtaut", null, null, null),
                        new TruckEntity("ANOTHERtruck", true, null, null, null),
                        1,
                        1,
                        1,
                        1
                ),
                Arguments.of(
                        new TruckTautlinerCouple("TRUCK123", "TAUT123"),
                        new TruckEntity("TRUCK123", true, null, null, null),
                        new TautlinerEntity(true, "TAUT123", null, null, null),
                        new TautlinerEntity(true, "ANOTHERtaut", null, null, null),
                        null,
                        1,
                        1,
                        1,
                        0
                ),
                Arguments.of(
                        new TruckTautlinerCouple("TRUCK123", "TAUT123"),
                        new TruckEntity("TRUCK123", true, null, null, null),
                        new TautlinerEntity(true, "TAUT123", null, null, null),
                        null,
                        new TruckEntity("ANOTHERtruck", true, null, null, null),
                        1,
                        1,
                        0,
                        1
                ),
                Arguments.of(
                        new TruckTautlinerCouple("TRUCK123", null),
                        new TruckEntity("TRUCK123", true, null, null, null),
                        null,
                        null,
                        null,
                        1,
                        0,
                        0,
                        0
                ),
                Arguments.of(
                        new TruckTautlinerCouple("TRUCK123", null),
                        new TruckEntity("TRUCK123", true, null, null, null),
                        null,
                        new TautlinerEntity(true, "ANOTHERtaut", null, null, null),
                        null,
                        1,
                        0,
                        1,
                        0
                )
        );
    }
}
//}String truckPlates, Boolean isMega, CarrierEntity carrier, TruckDriverEntity truckDriver, TautlinerEntity tautliner
//Boolean isXpo, String tautlinerPlates, LocalDateTime techInspection, CarrierEntity carrier, TruckEntity truck