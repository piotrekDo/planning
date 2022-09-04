package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AddTautlinerToCarrierTest {

    @Test
    void addTautlinerCommand_Should_throw_an_exception_if_adding_tautliner_with_carrier_assigned_already() {
        //given
        TautlinerEntity tautliner = new TautlinerEntity(69L, true, "TAUT321", LocalDateTime.now(), new CarrierEntity(), null);
        //when + then
        assertThrows(IllegalOperationException.class, () -> new AddTautlinerToCarrierAction(new CarrierEntity(), tautliner).execute());
    }

    @Test
    void addTautlinerCommand_should_add_carrier_free_tautliner() {
        //given
        CarrierEntity carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TautlinerEntity tautliner = new TautlinerEntity(69L, true, "TAUT321", LocalDateTime.now(), null, null);

        //when
        boolean result = new AddTautlinerToCarrierAction(carrier, tautliner).execute();

        //then
        assertTrue(result);
        assertEquals(1, carrier.getTautliners().size());
        assertEquals(tautliner, carrier.getTautliners().get(0));
        assertEquals(carrier, tautliner.getCarrier());
    }
}