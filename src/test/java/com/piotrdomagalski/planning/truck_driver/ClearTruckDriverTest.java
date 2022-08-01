package com.piotrdomagalski.planning.truck_driver;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ClearTruckDriverTest {

    @Test
    void clearTruckDriver_should_clean_driver_and_truck_and_carrier() {
        //given
        CarrierEntity carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckEntity truck = new TruckEntity(12L, "TEST1234", true, carrier, null, null);
        TruckDriverEntity driver = new TruckDriverEntity(321L, "Test Driver", "111111111", "ID543211", carrier, null);
        driver.setTruck(truck);
        truck.setTruckDriver(driver);
        carrier.getDrivers().add(driver);

        //when
        boolean result = new ClearTruckDriver(driver).execute();

        //then
        assertTrue(result);
        assertNull(driver.getTruck());
        assertNull(driver.getCarrier());
        assertNull(truck.getTruckDriver());
        assertEquals(Collections.emptyList(), carrier.getDrivers());

    }

}