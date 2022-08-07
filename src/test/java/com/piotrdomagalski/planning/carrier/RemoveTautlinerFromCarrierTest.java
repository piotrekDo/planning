package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class RemoveTautlinerFromCarrierTest {

    @Test
    void removeTautlinerCommand_should_throw_an_exception_when_removing_tautliner_which_doesnt_belong_to_carrier() {
        //given
        CarrierEntity carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        String tautlinersPlates = "NON999";

        //when+then
        assertThrows(NoSuchElementException.class, () -> new RemoveTautlinerFromCarrier(carrier, tautlinersPlates).execute());
    }

    @Test
    void removeTautlinerCommand_should_remove_tautliner_from_carrier() {
        //given
        CarrierEntity carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        String tautlinersPlates = "NON999";
        TautlinerEntity tautliner = new TautlinerEntity(69L, true, tautlinersPlates, LocalDateTime.now(), carrier, null);
        carrier.getTautliners().add(tautliner);

        //when
        boolean result = new RemoveTautlinerFromCarrier(carrier, tautlinersPlates).execute();

        //then
        assertTrue(result);
        assertEquals(Collections.emptyList(), carrier.getTautliners());
        assertNull(tautliner.getCarrier());
    }

}