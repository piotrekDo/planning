package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class RemoveTruckDriverFromCarrierTest {

    @Test
    void removeDriverCommand_should_throw_an_exception_when_trying_to_remove_driver_who_doesnt_belong_to_carreir() {
        //given
        CarrierEntity carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Long driversId = 432L;

        //when+then
        assertThrows(NoSuchElementException.class, () -> new RemoveTruckDriverFromCarrierAction(carrier, driversId).execute());
    }

    @Test
    void removeDriverCommand_should_remove_driver_from_carrier() {
        //given
        CarrierEntity carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Long driversId = 21L;
        TruckDriverEntity driver = new TruckDriverEntity(driversId, "Driver", "789789789", "ID123456", carrier, null);
        carrier.getDrivers().add(driver);

        //when
        boolean result = new RemoveTruckDriverFromCarrierAction(carrier, driversId).execute();

        //then
        assertTrue(result);
        assertEquals(Collections.emptyList(), carrier.getDrivers());
        assertNull(driver.getCarrier());
    }
}