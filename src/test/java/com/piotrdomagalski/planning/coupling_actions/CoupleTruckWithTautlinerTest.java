package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CoupleTruckWithTautlinerTest {

    @ParameterizedTest
    @ArgumentsSource(CoupleTruckWithTautlinerArgumentsProvider.class)
    void coupleTruckWithtautliner_for_the_same_carrier_should_return_true_and_set_trucks_tautliiner_and_tautliners_truck(
            CarrierEntity carrier,
            TruckEntity truck,
            TautlinerEntity tautliner) {
        //given
        truck.setCarrier(carrier);
        tautliner.setCarrier(carrier);

        //when
        boolean result = new CoupleTruckWithTautliner(truck, tautliner).couple();

        //then
        assertTrue(result);
        assertEquals(truck, tautliner.getTruck());
        assertEquals(tautliner, truck.getTautliner());
    }

    @Test
    void coupleTruckWithtautliner_for_no_carrier_xpo_tautliner_should_return_true_and_set_trucks_tautliiner_and_tautliners_truck() {
        //given
        CarrierEntity carrier = new CarrierEntity(1L, "123456", "Test Carrier", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckEntity truck = new TruckEntity(12L, "TEST1234", true, carrier, null, null);
        TautlinerEntity tautliner = new TautlinerEntity(9L, true, "ABCD1234", null, null, null);

        //when
        boolean result = new CoupleTruckWithTautliner(truck, tautliner).couple();

        //then
        assertTrue(result);
        assertEquals(truck, tautliner.getTruck());
        assertEquals(tautliner, truck.getTautliner());
    }

    @Test
    void coupleTruckWithtautliner_should_return_true_and_set_truck_and_tautliner_for_new_and_exising_values() {
        //given
        CarrierEntity carrier = new CarrierEntity(1L, "123456", "Test Carrier", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckEntity truck = new TruckEntity(12L, "TEST1234", true, carrier, null, null);
        TautlinerEntity tautlinerExisting = new TautlinerEntity(19L, false, "ABCD1235", null, carrier, truck);
        TruckEntity truckExisting = new TruckEntity(15L, "TEST12345", true, carrier, null, null);
        TautlinerEntity tautliner = new TautlinerEntity(9L, true, "ABCD1234", null, carrier, truckExisting);
        truckExisting.setTautliner(tautliner);
        truck.setTautliner(tautlinerExisting);

        //when
        boolean result = new CoupleTruckWithTautliner(truck, tautliner).couple();

        //then
        assertTrue(result);
        assertEquals(truck, tautliner.getTruck());
        assertEquals(tautliner, truck.getTautliner());
        assertNull(tautlinerExisting.getTruck());
        assertNull(truckExisting.getTautliner());
    }

    @Test
    void coupleTruckWithtautliner_should_throw_an_exception_when_coupling_non_xpo_tautliner_with_truck_with_different_carriers() {
        //given
        CarrierEntity carrier = new CarrierEntity(1L, "123456", "Test Carrier", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        CarrierEntity carrier2 = new CarrierEntity(11L, "123457", "Test Carrier2", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckEntity truck = new TruckEntity(12L, "TEST1234", true, carrier, null, null);
        TautlinerEntity tautliner = new TautlinerEntity(9L, false, "ABCD1235", null, carrier2, null);

        //when + then
        assertThrows(IllegalOperationException.class, () -> new CoupleTruckWithTautliner(truck, tautliner).couple());
        assertNull(truck.getTautliner());
        assertNull(tautliner.getTruck());
    }

    @Test
    void coupleTruckWithtautliner_should_throw_an_exception_when_coupling_xpo_tautliner_having_truck_with_different_carriers() {
        //given
        CarrierEntity carrier = new CarrierEntity(1L, "123456", "Test Carrier", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        CarrierEntity carrier2 = new CarrierEntity(11L, "123457", "Test Carrier2", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckEntity truck = new TruckEntity(12L, "TEST1234", true, carrier, null, null);
        TautlinerEntity xpoTautliner = new TautlinerEntity(9L, true, "ABCD1235", null, carrier, truck);
        TruckEntity truck2 = new TruckEntity(16L, "TEST1235", true, carrier2, null, null);

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> new CoupleTruckWithTautliner(truck2, xpoTautliner).couple());
        assertEquals("Cant couple xpo tautliner from another carrier if it has truck assigned, clear first", exception.getMessage());
        assertEquals(truck, xpoTautliner.getTruck());
        assertNull(truck2.getTautliner());
    }

}