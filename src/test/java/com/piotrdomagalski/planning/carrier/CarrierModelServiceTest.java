package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CarrierModelServiceTest {

    CarrierEntity carrier;
    CarrierModelService carrierModelService;


    @BeforeEach
    void set() {
        carrier = new CarrierEntity(990L, "123456", "Test carrier", "Testland", 1.1,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        carrierModelService = new CarrierModelService(carrier);
    }

    @Test
    void addTruck_Should_throw_an_exception_if_adding_truck_with_carrier_assigned_already() {
        //given
        CarrierEntity trucksCarreir = CarrierEntity.newCarrier("654321", "Other carrier", "Carrierland", 1.0);
        TruckEntity truck = new TruckEntity(1L, "TEST1234", true, trucksCarreir, null, null);

        //when + then
        assertThrows(IllegalOperationException.class, () -> carrierModelService.addTruck(truck));
    }

    @Test
    void addTruck_should_add_carrier_free_truck() {
        //given
        TruckEntity truck = new TruckEntity(1L, "TEST1234", true, null, null, null);

        //when
        boolean result = carrierModelService.addTruck(truck);

        //then
        assertTrue(result);
        assertEquals(1, carrier.getTrucks().size());
        assertEquals(truck, carrier.getTrucks().get(0));
        assertEquals(carrier, truck.getCarrier());
    }

    @Test
    void removeTruckByPlates_should_throw_an_exception_when_removing_truck_which_doesnt_belong_to_carrier() {
        //given
        String trucksPlates = "NON4322";

        //when+then
        assertThrows(NoSuchElementException.class, () -> carrierModelService.removeTruckByPlates(trucksPlates));
    }

    @Test
    void removeTruckByPlates_should_remove_truck_from_carrier() {
        //given
        String trucksPlates = "TEST1234";
        TruckEntity truck = new TruckEntity(1L, "TEST1234", true, carrier, null, null);
        carrier.getTrucks().add(truck);

        //when
        boolean result = carrierModelService.removeTruckByPlates(trucksPlates);

        //then
        assertTrue(result);
        assertEquals(Collections.emptyList(), carrier.getTrucks());
        assertNull(truck.getCarrier());
    }

    @Test
    void addDriver_should_throw_an_exception_when_adding_driver_having_carrier_already() {
        //given
        CarrierEntity driversCarrier = CarrierEntity.newCarrier("654321", "Other carrier", "Carrierland", 1.0);
        TruckDriverEntity driver = new TruckDriverEntity("Driver", "789789789", "ID123456", driversCarrier, null);

        //when+then
        assertThrows(IllegalOperationException.class, () -> carrierModelService.addDriver(driver));
    }

    @Test
    void addDriver_should_add_carrier_free_driver() {
        //given
        TruckDriverEntity driver = new TruckDriverEntity("Driver", "789789789", "ID123456", null, null);

        //when
        boolean result = carrierModelService.addDriver(driver);

        //then
        assertTrue(result);
        assertEquals(1, carrier.getDrivers().size());
        assertEquals(driver, carrier.getDrivers().get(0));
        assertEquals(carrier, driver.getCarrier());
    }

    @Test
    void removeDriverById_should_throw_an_exception_when_trying_to_remove_driver_who_doesnt_belong_to_carreir() {
        //given
        Long driversId = 432L;

        //when+then
        assertThrows(NoSuchElementException.class, () -> carrierModelService.removeDriverById(driversId));
    }

    @Test
    void removeDriverById_should_remove_driver_from_carrier() {
        //given
        Long driversId = 21L;
        TruckDriverEntity driver = new TruckDriverEntity(driversId, "Driver", "789789789", "ID123456", carrier, null);
        carrier.getDrivers().add(driver);

        //when
        boolean result = carrierModelService.removeDriverById(driversId);

        //then
        assertTrue(result);
        assertEquals(Collections.emptyList(), carrier.getDrivers());
        assertNull(driver.getCarrier());
    }

    @Test
    void addTautliner_Should_throw_an_exception_if_adding_tautliner_with_carrier_assigned_already() {
        //given
        CarrierEntity tautlinersCarreir = CarrierEntity.newCarrier("654321", "Other carrier", "Carrierland", 1.0);
        TautlinerEntity tautliner = new TautlinerEntity(69L, true, "TAUT321", LocalDateTime.now(), tautlinersCarreir, null);
        //when + then
        assertThrows(IllegalOperationException.class, () -> carrierModelService.addTautliner(tautliner));
    }

    @Test
    void addTautliner_should_add_carrier_free_tautliner() {
        //given
        TautlinerEntity tautliner = new TautlinerEntity(69L, true, "TAUT321", LocalDateTime.now(), null, null);

        //when
        boolean result = carrierModelService.addTautliner(tautliner);

        //then
        assertTrue(result);
        assertEquals(1, carrier.getTautliners().size());
        assertEquals(tautliner, carrier.getTautliners().get(0));
        assertEquals(carrier, tautliner.getCarrier());
    }

    @Test
    void removeTautlinerByPlates_should_throw_an_exception_when_removing_tautliner_which_doesnt_belong_to_carrier() {
        //given
        String tautlinersPlates = "NON999";

        //when+then
        assertThrows(NoSuchElementException.class, () -> carrierModelService.removeTautlinerByPlates(tautlinersPlates));
    }

    @Test
    void removeTautlinerByPlates_should_remove_tautliner_from_carrier() {
        //given
        String tautlinersPlates = "NON999";
        TautlinerEntity tautliner = new TautlinerEntity(69L, true, tautlinersPlates, LocalDateTime.now(), carrier, null);
        carrier.getTautliners().add(tautliner);

        //when
        boolean result = carrierModelService.removeTautlinerByPlates(tautlinersPlates);

        //then
        assertTrue(result);
        assertEquals(Collections.emptyList(), carrier.getTautliners());
        assertNull(tautliner.getCarrier());
    }
}