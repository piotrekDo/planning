package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ClearTruckTest {

    @ParameterizedTest
    @ArgumentsSource(TruckClearTruckArgumentsProvider.class)
    void clearTruckCommand_should_set_null_to_carrier_and_truck_and_driver_and_clean_them_as_well(TruckEntity truck,
                                                                                                  CarrierEntity carrier,
                                                                                                  TruckDriverEntity driver,
                                                                                                  TautlinerEntity tautliner) {
        //given
        truck.setCarrier(carrier);
        truck.setTruckDriver(driver);
        truck.setTautliner(tautliner);
        carrier.getTrucks().add(truck);
        driver.setTruck(truck);
        tautliner.setTruck(truck);

        //when
        boolean result = new ClearTruckAction(truck).execute();

        //then
        assertTrue(result);
        assertNull(truck.getCarrier());
        assertNull(truck.getTruckDriver());
        assertNull(truck.getTautliner());
        assertEquals(Collections.emptyList(), carrier.getTrucks());
        assertNull(driver.getTruck());
        assertNull(tautliner.getTruck());
    }

}