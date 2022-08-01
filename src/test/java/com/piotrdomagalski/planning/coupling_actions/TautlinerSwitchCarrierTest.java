package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TautlinerSwitchCarrierTest {

    @Test
    void tautlinerSwitchCarriers_should_switch_tautliners_carreir() {
        //given
        CarrierEntity carrier = new CarrierEntity(1L, "123456", "Test Carrier", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        CarrierEntity carrier2 = new CarrierEntity(11L, "123457", "Test Carrier2", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TautlinerEntity tautliner = new TautlinerEntity(9L, true, "ABCD1235", null, carrier, null);

        //when
        boolean result = new TautlinerSwitchCarrier(carrier2, tautliner).couple();

        //then
        assertTrue(result);
        assertEquals(carrier2, tautliner.getCarrier());
    }

    @Test
    void tautlinerSwitchCarriers_should_throw_an_exception_if_tautliner_has_truck() {
        //given
        CarrierEntity carrier = new CarrierEntity(11L, "123457", "Test Carrier2", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckEntity truck = new TruckEntity(12L, "TEST1234", true, carrier, null, null);
        TautlinerEntity tautliner = new TautlinerEntity(9L, true, "ABCD1235", null, carrier, truck);
        truck.setTautliner(tautliner);

        //given + when
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> new TautlinerSwitchCarrier(carrier, tautliner).couple());
        assertEquals("Tautliner has truck, uncouple first", exception.getMessage());
        assertEquals(truck, tautliner.getTruck());
        assertEquals(tautliner, truck.getTautliner());
    }

    @Test
    void tautlinerSwitchCarriers_should_throw_an_exception_if_tautliner_is_not_xpo() {
        //given
        CarrierEntity carrier = new CarrierEntity(11L, "123457", "Test Carrier2", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TautlinerEntity tautliner = new TautlinerEntity(9L, false, "ABCD1235", null, carrier, null);
        CarrierEntity carrier2 = new CarrierEntity(11L, "123457", "Test Carrier2", "Testowo", 1.1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());


        //given + when
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> new TautlinerSwitchCarrier(carrier2, tautliner).couple());
        assertEquals("Cannot switch carrier for non-xpo trailer", exception.getMessage());
        assertEquals(carrier, tautliner.getCarrier());
    }

}