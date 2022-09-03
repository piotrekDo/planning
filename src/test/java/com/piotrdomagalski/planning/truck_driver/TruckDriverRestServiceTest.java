package com.piotrdomagalski.planning.truck_driver;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierOperations;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class TruckDriverRestServiceTest {

    @TestConfiguration
    static class ReservationTestConfig {

        @Bean
        TruckDriverService tautlinerRestService(TruckDriverRepository truckDriverRepository,
                                                CarrierRepository carrierRepository, TruckDriverTransformer transformer,
                                                CarrierOperations carrierOperations) {
            return new TruckDriverService(truckDriverRepository, carrierRepository, transformer, carrierOperations);
        }
    }

    @MockBean
    TruckDriverRepository truckDriverRepository;

    @MockBean
    CarrierRepository carrierRepository;

    @MockBean
    TruckDriverTransformer transformer;

    @MockBean
    CarrierOperations carrierOperations;

    @Autowired
    TruckDriverService truckDriverRestService;


    @Test
    void getTruckDriverById_should_throw_an_exception_if_such_id_doesnt_exist() {
        //given
        Long id = 21L;

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckDriverRestService.getTruckDriverById(id));
    }

    @Test
    void getTruckDriverById_should_return_driver_if_found() {
        //given
        Long id = 32L;
        TruckDriverEntity entity = new TruckDriverEntity(32L, "Test driver", "555444333", "ID123456", null, null);
        TruckDriverInfoDTO dto = new TruckDriverInfoDTO(id, "Test driver", "555444333", "ID123456", null, null, null);
        Mockito.when(truckDriverRepository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.when(transformer.entityToDriverInfoDto(entity)).thenReturn(dto);

        //when
        TruckDriverInfoDTO result = truckDriverRestService.getTruckDriverById(id);

        //then
        assertEquals(dto, result);
        Mockito.verify(truckDriverRepository).findById(id);
        Mockito.verify(transformer).entityToDriverInfoDto(entity);
    }

    @Test
    void getAllTruckDrivers_should_return_an_empty_list_if_no_driver_is_present() {
        //when
        List<TruckDriverEntity> result = truckDriverRestService.getAllTruckDrivers();

        //then
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void addTruckDriver_will_throw_an_exception_when_wrong_carrier_provided() {
        //given
        String carrierSap = "123456";
        TruckDriverNewUpdateDTO driver = new TruckDriverNewUpdateDTO("Test one", "999000888", "ID123456");
        Mockito.when(carrierRepository.findBySap(carrierSap)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckDriverRestService.addNewDriver(carrierSap, driver));
        Mockito.verify(truckDriverRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    void addTruckDriver_will_add_new_entity() {
        //given
        TruckDriverNewUpdateDTO driver = new TruckDriverNewUpdateDTO("Test one", "999000888", "ID123456");
        TruckDriverEntity entity = new TruckDriverEntity("Test one", "999000888", "ID123456", null, null);
        CarrierEntity carrier = new CarrierEntity(2L, "123456", "Test trans", "Testland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckDriverEntity savedEntity = new TruckDriverEntity(1L, "Test one", "999000888", "ID123456", carrier, null);
        Mockito.when(transformer.newUpdatDriverDtoToEntity(driver)).thenReturn(entity);
        Mockito.when(carrierRepository.findBySap(carrier.getSap())).thenReturn(Optional.of(carrier));
        Mockito.when(truckDriverRepository.save(entity)).thenReturn(savedEntity);
        Mockito.when(transformer.entityToNewUpdateDriverDto(savedEntity)).thenReturn(driver);
        Mockito.when(carrierOperations.addDriver(carrier, entity)).thenReturn(true);

        //when
        TruckDriverNewUpdateDTO result = truckDriverRestService.addNewDriver(carrier.getSap(), driver);

        //then
        assertEquals(driver, result);
        Mockito.verify(transformer).newUpdatDriverDtoToEntity(driver);
        Mockito.verify(carrierRepository).findBySap(carrier.getSap());
        Mockito.verify(truckDriverRepository).save(entity);
        Mockito.verify(transformer).entityToNewUpdateDriverDto(savedEntity);
        Mockito.verify(carrierOperations).addDriver(carrier, entity);
    }

    @Test
    void deleteTruckDriverById_should_throw_an_exception_if_no_such_id_present() {
        //given
        Long id = 21L;

        //when + theb
        assertThrows(NoSuchElementException.class, () -> truckDriverRestService.deleteTruckDriverById(id));
        Mockito.verify(truckDriverRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteTruckDriverById_should_delete_driver_if_exists() {
        //given
        Long id = 32L;
        TruckDriverEntity entity = new TruckDriverEntity(32L, "Test one", "999000888", "ID123456", null, null);
        Mockito.when(truckDriverRepository.findById(id)).thenReturn(Optional.of(entity));

        //when
        TruckDriverEntity result = truckDriverRestService.deleteTruckDriverById(id);

        //then
        assertEquals(entity, result);
        Mockito.verify(truckDriverRepository).findById(id);
        Mockito.verify(truckDriverRepository).delete(entity);
    }

    @Test
    void updateTruckDriver_should_throw_an_error_if_no_such_id_exists() {
        //given
        Long id = 312L;
        TruckDriverNewUpdateDTO dto = new TruckDriverNewUpdateDTO("Name", "123321231", "ID123456");
        Mockito.when(truckDriverRepository.findById(id)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckDriverRestService.updateTruckDriver(id, dto));
        Mockito.verify(truckDriverRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(transformer, Mockito.never()).newUpdatDriverDtoToEntity(Mockito.any());
        Mockito.verify(transformer, Mockito.never()).entityToNewUpdateDriverDto(Mockito.any());
    }

    @ParameterizedTest
    @ArgumentsSource(TruckDriverUpdateArgumentsProvider.class)
    void updateTruckDriver_shuld_update_existing_driver(Long id, TruckDriverEntity foundById,
                                                        TruckDriverNewUpdateDTO provided,
                                                        TruckDriverEntity translatedToEntity,
                                                        TruckDriverNewUpdateDTO expected) {
        //given
        Mockito.when(truckDriverRepository.findById(id)).thenReturn(Optional.of(foundById));
        Mockito.when(transformer.newUpdatDriverDtoToEntity(provided)).thenReturn(translatedToEntity);
        Mockito.when(truckDriverRepository.save(foundById)).thenReturn(foundById);
        Mockito.when(transformer.entityToNewUpdateDriverDto(foundById)).thenReturn(expected);

        //when
        TruckDriverNewUpdateDTO result = truckDriverRestService.updateTruckDriver(id, provided);

        //then
        assertEquals(expected, result);
        Mockito.verify(truckDriverRepository).findById(id);
        Mockito.verify(transformer).newUpdatDriverDtoToEntity(provided);
        Mockito.verify(truckDriverRepository).save(foundById);
        Mockito.verify(transformer).entityToNewUpdateDriverDto(foundById);

    }

}