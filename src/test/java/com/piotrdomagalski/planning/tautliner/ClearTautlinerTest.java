package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ClearTautlinerTest {

    @ParameterizedTest
    @ArgumentsSource(TautlinerClearTautlinerArgumentsProvider.class)
    void clearTautlinerCommand_should_set_null_to_carrier_and_truck_and_clean_them_as_well(TautlinerEntity tautliner,
                                                                                           TruckEntity tautTruck,
                                                                                           CarrierEntity tautCarrier) {
        //given
        tautliner.setCarrier(tautCarrier);
        tautCarrier.getTautliners().add(tautliner);
        tautliner.setTruck(tautTruck);
        tautTruck.setTautliner(tautliner);

        //when
        boolean result = new ClearTautliner(tautliner).execute();

        //then
        assertTrue(result);
        assertNull(tautliner.getCarrier());
        assertNull(tautliner.getTruck());
        assertNull(tautTruck.getTautliner());
        assertEquals(Collections.emptyList(), tautCarrier.getTrucks());
    }

}