package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TruckModelServiceTest {


    TruckEntity truckEntity;
    TruckModelService truckModelService;

    @BeforeEach
    void set() {
        truckEntity = new TruckEntity("TEST123", true, null, null, null);
        truckModelService = new TruckModelService(truckEntity);
    }

    @ParameterizedTest
    @ArgumentsSource(TruckClearTruckArgumentsProvider.class)
    void clearTruck_should_set_null_to_carrier_and_truck_and_driver_and_clean_them_as_well(TruckEntity truck,
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
        truckModelService.setTruck(truck);

        //when
        boolean result = truckModelService.clearTruck();

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