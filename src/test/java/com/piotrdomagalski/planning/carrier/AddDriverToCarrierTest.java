package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AddDriverToCarrierTest {

    @Test
    void calling_addDriverToCarrierCommand_should_throw_an_exception_if_driver_has_carrier_already() {
        TruckDriverEntity driver = new TruckDriverEntity("Driver", "789789789", "ID123456", new CarrierEntity(), null);


        //when+then
        assertThrows(IllegalOperationException.class, () -> new AddDriverToCarrier(new CarrierEntity(), driver).execute());
    }

    @Test
    void calling_addDriverToCarrierCommand_should_add_carrier_free_driver() {
        //given
        CarrierEntity carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckDriverEntity driver = new TruckDriverEntity("Driver", "789789789", "ID123456", null, null);

        //when
        boolean result = new AddDriverToCarrier(carrier, driver).execute();

        //then
        assertTrue(result);
        assertEquals(1, carrier.getDrivers().size());
        assertEquals(driver, carrier.getDrivers().get(0));
        assertEquals(carrier, driver.getCarrier());
    }
}