package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CoupleTruckWithDriverTest {

    @Test
    void coupleTruckWithDriver_should_return_true_and_set_truck_for_driver_and_driver_for_truck() {
        //given
        CarrierEntity carrier = new CarrierEntity(1L, "123456", "Test Carrier", "Testowo", 1.1, null, null, null);
        TruckEntity truck = new TruckEntity(12L, "TEST1234", true, carrier, null, null);
        TruckDriverEntity driver = new TruckDriverEntity(10L, "Test Driver", "555-555-555", "ID123456", carrier, null);

        //when
        boolean result = new CoupleTruckWithDriver(driver, truck).couple();

        //then
        assertTrue(result);
        assertEquals(driver, truck.getTruckDriver());
        assertEquals(truck, driver.getTruck());
    }

    @Test
    void coupleTruckWithDriver_should_return_true_and_set_truck_for_driver_and_driver_for_truck_and_clean_existing_truck_and_driver(){
        //given
        CarrierEntity carrier = new CarrierEntity(1L, "123456", "Test Carrier", "Testowo", 1.1, null, null, null);
        TruckEntity truck = new TruckEntity(12L, "TEST1234", true, carrier, null, null);
        TruckDriverEntity driverExisting = new TruckDriverEntity(19L, "Test Driver2", "555-555-565", "ID123457", carrier, truck);
        TruckEntity truckExisting = new TruckEntity(15L, "TEST1235", true, carrier, null, null);
        TruckDriverEntity driver = new TruckDriverEntity(10L, "Test Driver", "555-555-555", "ID123456", carrier, truckExisting);
        truckExisting.setTruckDriver(driver);
        truck.setTruckDriver(driverExisting);

        //when
        boolean result = new CoupleTruckWithDriver(driver, truck).couple();

        //then
        assertTrue(result);
        assertEquals(driver, truck.getTruckDriver());
        assertEquals(truck, driver.getTruck());
        assertNull(truckExisting.getTruckDriver());
        assertNull(driverExisting.getTruck());
    }

    @Test
    void coupleTruckWithDriver_should_throw_an_exception_when_coupling_driver_and_truck_with_different_carriers() {
        //given
        CarrierEntity carrier = new CarrierEntity(1L, "123456", "Test Carrier", "Testowo", 1.1, null, null, null);
        CarrierEntity carrier2 = new CarrierEntity(2L, "123457", "Test Carrier2", "Testowo", 1.1, null, null, null);
        TruckEntity truck = new TruckEntity(12L, "TEST1234", true, carrier, null, null);
        TruckDriverEntity driver = new TruckDriverEntity(10L, "Test Driver", "555-555-555", "ID123456", carrier2, null);

        //when + then
        assertThrows(IllegalOperationException.class, () -> new CoupleTruckWithDriver(driver, truck).couple());
        assertNull(truck.getTruckDriver());
        assertNull(driver.getTruck());
    }

}