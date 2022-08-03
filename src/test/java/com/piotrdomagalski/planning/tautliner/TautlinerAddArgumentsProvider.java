package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Stream;

public class TautlinerAddArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //input
                //entity
                //carrier
                Arguments.of(
                        new TautlinerNewUpdateDTO(true, "ABC1234", "10-10-2022"),
                        new TautlinerEntity(1L, true, "ABC1234",
                                LocalDateTime.of(2022, 10, 10, 0, 0, 0),
                                null, null),
                        new CarrierEntity(15L, "123456", "Test carrier", "Testland", 1.3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                ),
                Arguments.of(
                        new TautlinerNewUpdateDTO(true, "CDE981", "05-05-2020"),
                        new TautlinerEntity(1L, true, "CDE981",
                                LocalDateTime.of(2020, 5, 5, 0, 0, 0),
                                null, null),
                        new CarrierEntity(15L, "123456", "Test carrier", "Testland", 1.3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                )
        );
    }
}
