package com.piotrdomagalski.planning.truck_driver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

class TruckDriverGetAllArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new TruckDriverEntity("Test driver", "555444333", "ID123456", null, null)
                        ),
                        List.of(
                                new TruckDriverEntity("Test driver", "555444333", "ID123456", null, null)
                        )
                ),
                Arguments.of(
                        List.of(
                                new TruckDriverEntity("ABC driver", "555444333", "ID123456", null, null),
                                new TruckDriverEntity("EDF driver", "555444333", "ID123456", null, null),
                                new TruckDriverEntity("CDA driver", "555444333", "ID123456", null, null),
                                new TruckDriverEntity("DEF driver", "555444333", "ID123456", null, null),
                                new TruckDriverEntity("BCA driver", "555444333", "ID123456", null, null)
                        ),
                        List.of(
                                new TruckDriverEntity(1L, "ABC driver", "555444333", "ID123456", null, null),
                                new TruckDriverEntity(5L, "BCA driver", "555444333", "ID123456", null, null),
                                new TruckDriverEntity(3L, "CDA driver", "555444333", "ID123456", null, null),
                                new TruckDriverEntity(4L, "DEF driver", "555444333", "ID123456", null, null),
                                new TruckDriverEntity(2L, "EDF driver", "555444333", "ID123456", null, null)
                        )
                )
        );
    }
}
