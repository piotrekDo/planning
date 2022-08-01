package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CarrierModelServiceTest {

    CarrierEntity carrierTest;
    CarrierModelService carrierModelService;

    @BeforeEach
    void clean() {
        carrierTest = new CarrierEntity(99L, "123456", "Test Carrier", "Testland", 1.2,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        carrierModelService = new CarrierModelService(carrierTest);
    }

    @Test
    void addTruck_shoud_throw_and_exception_if_truck_has_carrier_already() {
        //given
        CarrierEntity existingCarrier = new CarrierEntity(22L, "654321", "Existing", "Somewhere", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckEntity truck = new TruckEntity(200L, "ABC1234", existingCarrier, null, null);

        //when + then
        assertThrows(IllegalOperationException.class, () -> carrierModelService.addTruck(truck));
    }

    @Test
    void addTruck_should_add_truck_to_driver_and_set_carrier_in_truck() {
        //given
        TruckEntity truck = new TruckEntity(200L, "ABC1234", null, null, null);

        //when
        carrierModelService.addTruck(truck);

        //then
        assertEquals(carrierTest.getTrucks().get(0), truck);
        assertEquals(truck.getCarrier(), carrierTest);
    }

    @Test
    void removeTruck_should_throw_an_exception_when_truck_doesnt_exist() {
        //given
        Long truckId = 200L;

        //when + then
        assertThrows(NoSuchElementException.class, () -> carrierModelService.removeTruck(truckId));
    }

    @ParameterizedTest
    @ArgumentsSource(CarrierRemoveTruckArgumentsProvider.class)
    void removeTruck_should_remove_truck_from_carrier_and_clear_tautliner_and_driver(CarrierEntity carrier, Long truckId,
                                                                                     TruckEntity truckEntityById) {
        //given
        carrier.getTrucks().add(truckEntityById);
        carrierModelService.setCarrier(carrier);

        //when
        TruckEntity result = carrierModelService.removeTruck(truckId);

        //then
        assertEquals(truckEntityById, result);
        assertEquals(0, carrier.getTrucks().size());
        assertNull(result.getCarrier());
        assertNull(result.getTautliner());
        assertNull(result.getTruckDriver());
    }

    @Test
    void addDriver_should_throw_an_exception_if_driver_has_carrier() {
        //given
        Long driverId = 220L;

        //when + then
        assertThrows(NoSuchElementException.class, ()-> carrierModelService.removeDriver(driverId));
    }

    @ParameterizedTest
    @ArgumentsSource(CarrierRemoveDriverArgumentsProvider.class)
    void removeDriver_should_remove_and_clear_driver(){

    }


}