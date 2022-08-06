package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CarrierClearCarrierArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //Carrier
                //List of drivers
                //List of trucks
                //List of tautliners
                Arguments.of(
                        new CarrierEntity(12L, "123456", "Carrier One", "Testland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        List.of(
                        )
                )
        );
    }
}
