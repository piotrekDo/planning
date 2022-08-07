package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class RemoveTruckFromCarrierTest {

    @Test
    void removeTruckCommand_should_throw_an_exception_when_removing_truck_which_doesnt_belong_to_carrier() {
        //given
        CarrierEntity carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        String trucksPlates = "NON4322";

        //when+then
        assertThrows(NoSuchElementException.class, () -> new RemoveTruckFromCarrier(carrier, trucksPlates).execute());
    }

    @Test
    void removeTruckCommand_should_remove_truck_from_carrier() {
        //given
        CarrierEntity carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        String trucksPlates = "TEST1234";
        TruckEntity truck = new TruckEntity(1L, "TEST1234", true, carrier, null, null);
        carrier.getTrucks().add(truck);

        //when
        boolean result = new RemoveTruckFromCarrier(carrier, trucksPlates).execute();

        //then
        assertTrue(result);
        assertEquals(Collections.emptyList(), carrier.getTrucks());
        assertNull(truck.getCarrier());
    }

}