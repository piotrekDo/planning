package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AddTruckToCarrierTest {

    @Test
    void addTruckCommand_Should_throw_an_exception_if_adding_truck_with_carrier_assigned_already() {
        //given
        TruckEntity truck = new TruckEntity(1L, "TEST1234", true, new CarrierEntity(), null, null);

        //when + then
        assertThrows(IllegalOperationException.class, () -> new AddTruckToCarrier(new CarrierEntity(), truck).execute());
    }

    @Test
    void addTruckCommand_should_add_carrier_free_truck() {
        //given
        CarrierEntity carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckEntity truck = new TruckEntity(1L, "TEST1234", true, null, null, null);

        //when
        boolean result = new AddTruckToCarrier(carrier, truck).execute();

        //then
        assertTrue(result);
        assertEquals(1, carrier.getTrucks().size());
        assertEquals(truck, carrier.getTrucks().get(0));
        assertEquals(carrier, truck.getCarrier());
    }
}