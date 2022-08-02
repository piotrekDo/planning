package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
class TautlinerModelServiceTest {

    TautlinerEntity tautlinerTest;
    TautlinerModelService tautlinerModelService;

    @BeforeEach
    void clean() {
        tautlinerTest = new TautlinerEntity(1L, true, "APL1234", LocalDateTime.now(), null, null);
        tautlinerModelService = new TautlinerModelService(tautlinerTest);
    }

    @ParameterizedTest
    @ArgumentsSource(TautlinerClearTautlinerArgumentsProvider.class)
    void clearTautliner_should_set_null_to_carrier_and_truck_and_clean_them_as_well(TautlinerEntity tautliner,
                                                                                    TruckEntity tautTruck,
                                                                                    CarrierEntity tautCarrier) {
        //given
        tautliner.setCarrier(tautCarrier);
        tautCarrier.getTautliners().add(tautliner);
        tautliner.setTruck(tautTruck);
        tautTruck.setTautliner(tautliner);
        tautlinerModelService.setTautliner(tautliner);

        //when
        boolean result = tautlinerModelService.clearTautliner();

        //then
        assertTrue(result);
        assertNull(tautliner.getCarrier());
        assertNull(tautliner.getTruck());
        assertNull(tautTruck.getTautliner());
        assertEquals(Collections.emptyList(), tautCarrier.getTrucks());
    }

}