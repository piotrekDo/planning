package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.stream.Stream;

public class CarrierRemoveTruckArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //carrier
                //truck id
                //truck by id
                //tautliner
                //driver
                Arguments.of(
                        new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2,
                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        100L,
                        new TruckEntity(100L, "ABC1234",
                                new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                                null, null)
                ),
                Arguments.of(
                        new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2,
                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        100L,
                        new TruckEntity(100L, "ABC1234",
                                new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                                new TruckDriverEntity(), new TautlinerEntity())
                ),
                Arguments.of(
                        new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2,
                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        100L,
                        new TruckEntity(100L, "ABC1234",
                                new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                                new TruckDriverEntity(), null)
                ),
                Arguments.of(
                        new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2,
                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                        100L,
                        new TruckEntity(100L, "ABC1234",
                                new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                                null, new TautlinerEntity())
                )
        );
    }
}


//                        new TautlinerEntity(200L,false,"ABC1234",LocalDate.now(),null,null),
//                                new TruckDriverEntity(300L,"Brewery Stinkyrash","123456789","ID123456",null,null)