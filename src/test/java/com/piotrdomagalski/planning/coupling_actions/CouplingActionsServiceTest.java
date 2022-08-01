package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerRepository;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckRepository;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CouplingActionsServiceTest {

    @TestConfiguration
    static class CouplingTestConfig {

        @Bean
        CouplingActionsService couplingActionsService(CouplingActions couplingActions, CarrierRepository carrierRepository,
                                                      TruckDriverRepository truckDriverRepository, TruckRepository truckRepository,
                                                      TautlinerRepository tautlinerRepository) {
            return new CouplingActionsService(couplingActions, carrierRepository, truckDriverRepository, truckRepository, tautlinerRepository);
        }
    }

    @Autowired
    CouplingActionsService couplingActionsService;

    @MockBean
    CouplingActions couplingActions;

    @MockBean
    CarrierRepository carrierRepository;

    @MockBean
    TruckDriverRepository truckDriverRepository;

    @MockBean
    TruckRepository truckRepository;

    @MockBean
    TautlinerRepository tautlinerRepository;


    @Test
    void coupleTruckDriver_should_couple_if_truck_and_driver_found() {
        //given
        TruckDriverCouple couple = new TruckDriverCouple("TEST1234", 12L);
        TruckEntity truck = TruckEntity.newTruck(couple.getTruck(), false);
        TruckDriverEntity driver = TruckDriverEntity.newTruckDriver("Test Driver", "777-777-777", "ID123456");
        Mockito.when(truckDriverRepository.findById(couple.getDriver())).thenReturn(Optional.of(driver));
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck())).thenReturn(Optional.of(truck));

        //when
        TruckDriverCouple result = couplingActionsService.coupleTruckDriver(couple);

        //then
        assertEquals(couple, result);
        Mockito.verify(truckDriverRepository).findById(couple.getDriver());
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(couple.getTruck());
        Mockito.verify(couplingActions).coupleTruckWithDriver(driver, truck);
        Mockito.verify(truckDriverRepository).save(driver);
        Mockito.verify(truckRepository).save(truck);
    }

    @Test
    void coupleTruckDriver_should_throw_an_exception_when_no_driver_found() {
        //given
        TruckDriverCouple couple = new TruckDriverCouple("TEST1234", 12L);
        TruckEntity truck = TruckEntity.newTruck(couple.getTruck(), false);
        TruckDriverEntity driver = TruckDriverEntity.newTruckDriver("Test Driver", "777-777-777", "ID123456");
        Mockito.when(truckDriverRepository.findById(couple.getDriver())).thenReturn(Optional.empty());
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck())).thenReturn(Optional.of(truck));

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> couplingActionsService.coupleTruckDriver(couple));
        assertEquals("No driver found with id: " + couple.getDriver(), exception.getMessage());
    }

    @Test
    void coupleTruckDriver_should_throw_an_exception_when_no_truck_found() {
        //given
        TruckDriverCouple couple = new TruckDriverCouple("TEST1234", 12L);
        TruckEntity truck = TruckEntity.newTruck(couple.getTruck(), false);
        TruckDriverEntity driver = TruckDriverEntity.newTruckDriver("Test Driver", "777-777-777", "ID123456");
        Mockito.when(truckDriverRepository.findById(couple.getDriver())).thenReturn(Optional.of(driver));
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck())).thenReturn(Optional.empty());

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> couplingActionsService.coupleTruckDriver(couple));
        assertEquals("No truck found with plates: " + couple.getTruck(), exception.getMessage());
    }

    @Test
    void coupleTruckDriver_should_throw_an_exception_when_truck_and_driver_dont_have_the_same_carrier() {
        //given
        TruckDriverCouple couple = new TruckDriverCouple("TEST1234", 12L);
        TruckEntity truck = TruckEntity.newTruck(couple.getTruck(), false);
        TruckDriverEntity driver = TruckDriverEntity.newTruckDriver("Test Driver", "777-777-777", "ID123456");
        Mockito.when(truckDriverRepository.findById(couple.getDriver())).thenReturn(Optional.of(driver));
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck())).thenReturn(Optional.of(truck));
        Mockito.doThrow(new IllegalOperationException("Truck and driver must have the same carrier!"))
                .when(couplingActions).coupleTruckWithDriver(driver, truck);

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> couplingActionsService.coupleTruckDriver(couple));
        assertEquals("Truck and driver must have the same carrier!", exception.getMessage());
        Mockito.verify(truckDriverRepository).findById(couple.getDriver());
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(couple.getTruck());
        Mockito.verify(truckDriverRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(truckRepository, Mockito.never()).save(Mockito.any());
    }


    @Test
    void coupleTruckTautliner_should_couple_if_truck_and_tautliner_found() {
        //given
        TruckTautlinerCouple couple = new TruckTautlinerCouple("TEST1234", "NACZ1234");
        TruckEntity truck = TruckEntity.newTruck(couple.getTruck(), false);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(false, couple.getTautliner(), LocalDateTime.now());
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.of(tautliner));
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck())).thenReturn(Optional.of(truck));

        //when
        TruckTautlinerCouple result = couplingActionsService.coupleTruckTautliner(couple);

        //then
        assertEquals(couple, result);
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(couple.getTautliner());
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(couple.getTruck());
        Mockito.verify(couplingActions).coupleTruckWithTautliner(truck, tautliner);
        Mockito.verify(tautlinerRepository).save(tautliner);
        Mockito.verify(truckRepository).save(truck);
    }

    @Test
    void coupleTruckTautliner_should_throw_an_exception_when_no_tautliner_found() {
        //given
        TruckTautlinerCouple couple = new TruckTautlinerCouple("TEST1234", "NACZ1234");
        TruckEntity truck = TruckEntity.newTruck(couple.getTruck(), false);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, couple.getTautliner(), LocalDateTime.now());
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.empty());
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck())).thenReturn(Optional.of(truck));

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> couplingActionsService.coupleTruckTautliner(couple));
        assertEquals("No tautliner found with plates: " + couple.getTautliner(), exception.getMessage());
    }

    @Test
    void ccoupleTruckTautliner_should_throw_an_exception_when_no_truck_found() {
        //given
        TruckTautlinerCouple couple = new TruckTautlinerCouple("TEST1234", "NACZ1234");
        TruckEntity truck = TruckEntity.newTruck(couple.getTruck(), false);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, couple.getTautliner(), LocalDateTime.now());
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.of(tautliner));
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck())).thenReturn(Optional.empty());

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> couplingActionsService.coupleTruckTautliner(couple));
        assertEquals("No truck found with plates: " + couple.getTruck(), exception.getMessage());
    }

    @Test
    void ccoupleTruckTautliner_should_throw_an_exception_when_non_xpo_tautliner_has_other_carrier() {
        //given
        TruckTautlinerCouple couple = new TruckTautlinerCouple("TEST1234", "NACZ1234");
        TruckEntity truck = TruckEntity.newTruck(couple.getTruck(), false);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(false, couple.getTautliner(), LocalDateTime.now());
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.of(tautliner));
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck())).thenReturn(Optional.of(truck));
        Mockito.doThrow(new IllegalOperationException("Cant couple non-xpo tautliner  with another carrier's truck"))
                .when(couplingActions).coupleTruckWithTautliner(truck, tautliner);

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> couplingActionsService.coupleTruckTautliner(couple));
        assertEquals("Cant couple non-xpo tautliner  with another carrier's truck", exception.getMessage());
        Mockito.verify(truckRepository, Mockito.never()).save(truck);
        Mockito.verify(tautlinerRepository, Mockito.never()).save(tautliner);
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(couple.getTruck());
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(couple.getTautliner());
    }

    @Test
    void ccoupleTruckTautliner_should_throw_an_exception_when_xpo_tautliner_has_other_truck() {
        //given
        TruckTautlinerCouple couple = new TruckTautlinerCouple("TEST1234", "NACZ1234");
        TruckEntity truck = TruckEntity.newTruck(couple.getTruck(), false);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, couple.getTautliner(), LocalDateTime.now());
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.of(tautliner));
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck())).thenReturn(Optional.of(truck));
        Mockito.doThrow(new IllegalOperationException("Cant couple xpo tautliner from another carrier if it has truck assigned, clear first"))
                .when(couplingActions).coupleTruckWithTautliner(truck, tautliner);

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> couplingActionsService.coupleTruckTautliner(couple));
        assertEquals("Cant couple xpo tautliner from another carrier if it has truck assigned, clear first", exception.getMessage());
        Mockito.verify(truckRepository, Mockito.never()).save(truck);
        Mockito.verify(tautlinerRepository, Mockito.never()).save(tautliner);
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(couple.getTruck());
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(couple.getTautliner());
    }

    @Test
    void coupleTautlinerCarrier_should_couple_if_carrier_and_tautliner_found() {
        //given
        TautlinerCarrierCouple couple = new TautlinerCarrierCouple("TAUT1234", "123456");
        CarrierEntity carrier = CarrierEntity.newCarrier(couple.getCarrierSap(), "Test Trans", "Testland", 1.2);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, couple.getTautliner(), LocalDateTime.now());
        Mockito.when(carrierRepository.findBySap(couple.getCarrierSap())).thenReturn(Optional.of(carrier));
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.of(tautliner));

        //when
        TautlinerCarrierCouple result = couplingActionsService.coupleTautlinerCarrier(couple);

        //then
        assertEquals(couple, result);
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(couple.getTautliner());
        Mockito.verify(carrierRepository).findBySap(couple.getCarrierSap());
        Mockito.verify(couplingActions).switchTautlinerCarrier(carrier, tautliner);
        Mockito.verify(tautlinerRepository).save(tautliner);
    }

    @Test
    void coupleTautlinerCarrier_should_throw_an_exception_when_no_tautliner_found() {
        //given
        TautlinerCarrierCouple couple = new TautlinerCarrierCouple("TAUT1234", "123456");
        CarrierEntity carrier = CarrierEntity.newCarrier(couple.getCarrierSap(), "Test Trans", "Testland", 1.2);
        Mockito.when(carrierRepository.findBySap(couple.getCarrierSap())).thenReturn(Optional.of(carrier));
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.empty());

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> couplingActionsService.coupleTautlinerCarrier(couple));
        assertEquals("No tautliner found with plates: " + couple.getTautliner(), exception.getMessage());
    }

    @Test
    void coupleTautlinerCarrier_should_throw_an_exception_when_no_carrier_found() {
        //given
        TautlinerCarrierCouple couple = new TautlinerCarrierCouple("TAUT1234", "123456");
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, couple.getTautliner(), LocalDateTime.now());
        Mockito.when(carrierRepository.findBySap(couple.getCarrierSap())).thenReturn(Optional.empty());
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.of(tautliner));

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> couplingActionsService.coupleTautlinerCarrier(couple));
        assertEquals("No carrier found with id: " + couple.getCarrierSap(), exception.getMessage());
        Mockito.verify(tautlinerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void coupleTautlinerCarrier_should_switch_carrier_to_null_if_tautliner_is_xpo() {
        //given
        TautlinerCarrierCouple couple = new TautlinerCarrierCouple("TAUT1234", null);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, couple.getTautliner(), LocalDateTime.now());
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.of(tautliner));

        //when
        TautlinerCarrierCouple result = couplingActionsService.coupleTautlinerCarrier(couple);

        //then
        assertEquals(couple, result);
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(couple.getTautliner());
        Mockito.verify(carrierRepository, Mockito.never()).findBySap(Mockito.any());
        Mockito.verify(tautlinerRepository).save(tautliner);
    }

    @Test
    void coupleTautlinerCarrier_should_throw_an_exception_when_carrier_is_null_and_tautliner_non_xpo() {
        TautlinerCarrierCouple couple = new TautlinerCarrierCouple("TAUT1234", null);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(false, couple.getTautliner(), LocalDateTime.now());
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.of(tautliner));
        Mockito.when(carrierRepository.findBySap(couple.getCarrierSap())).thenReturn(Optional.empty());

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> couplingActionsService.coupleTautlinerCarrier(couple));
        assertEquals("No carrier found with id: " + couple.getCarrierSap(), exception.getMessage());
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(couple.getTautliner());
        Mockito.verify(carrierRepository).findBySap(couple.getCarrierSap());
        Mockito.verify(tautlinerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void coupleTautlinerCarrier_should_throw_an_exception_when_tautliner_has_truck() {
        TautlinerCarrierCouple couple = new TautlinerCarrierCouple("TAUT1234", null);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(false, couple.getTautliner(), LocalDateTime.now());
        CarrierEntity carrier = CarrierEntity.newCarrier(couple.getCarrierSap(), "Test Trans", "Testland", 1.2);
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.of(tautliner));
        Mockito.when(carrierRepository.findBySap(couple.getCarrierSap())).thenReturn(Optional.of(carrier));
        Mockito.doThrow(new IllegalOperationException("Tautliner has truck, uncouple first"))
                .when(couplingActions).switchTautlinerCarrier(carrier, tautliner);

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> couplingActionsService.coupleTautlinerCarrier(couple));
        assertEquals("Tautliner has truck, uncouple first", exception.getMessage());
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(couple.getTautliner());
        Mockito.verify(carrierRepository).findBySap(couple.getCarrierSap());
        Mockito.verify(couplingActions).switchTautlinerCarrier(carrier, tautliner);
        Mockito.verify(tautlinerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void coupleTautlinerCarrier_should_throw_an_exception_when_trying_to_couple_non_xpo_tautliner_with_another_carrier() {
        TautlinerCarrierCouple couple = new TautlinerCarrierCouple("TAUT1234", null);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(false, couple.getTautliner(), LocalDateTime.now());
        CarrierEntity carrier = CarrierEntity.newCarrier(couple.getCarrierSap(), "Test Trans", "Testland", 1.2);
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner())).thenReturn(Optional.of(tautliner));
        Mockito.when(carrierRepository.findBySap(couple.getCarrierSap())).thenReturn(Optional.of(carrier));
        Mockito.doThrow(new IllegalOperationException("Cannot switch carrier for non-xpo trailer"))
                .when(couplingActions).switchTautlinerCarrier(carrier, tautliner);

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> couplingActionsService.coupleTautlinerCarrier(couple));
        assertEquals("Cannot switch carrier for non-xpo trailer", exception.getMessage());
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(couple.getTautliner());
        Mockito.verify(carrierRepository).findBySap(couple.getCarrierSap());
        Mockito.verify(couplingActions).switchTautlinerCarrier(carrier, tautliner);
        Mockito.verify(tautlinerRepository, Mockito.never()).save(Mockito.any());
    }
}