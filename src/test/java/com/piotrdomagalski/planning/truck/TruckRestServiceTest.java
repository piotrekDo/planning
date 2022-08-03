package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TruckRestServiceTest {

    @TestConfiguration
    static class TruckServiceTestConfig {

        @Bean
        TruckRestService truckRestService(TruckRepository truckRepository, CarrierRepository carrierRepository,
                                          TruckTransformer transformer) {
            return new TruckRestService(truckRepository, carrierRepository, transformer);
        }
    }

    @Autowired
    TruckRestService truckRestService;

    @MockBean
    TruckTransformer transformer;

    @MockBean
    TruckRepository truckRepository;

    @MockBean
    CarrierRepository carrierRepository;

    @Test
    void getAllTrucks_should_return_an_empty_list_when_no_trucks_present() {
        //when
        List<TruckEntity> result = truckRestService.getAllTrucks();

        //then
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void getTruckById_should_throw_an_exception_when_there_is_no_truck_with_such_id() {
        //given
        Long id = 43L;
        Mockito.when(truckRepository.findById(id)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckRestService.getTruckById(id));
    }

    @Test
    void getTruckById_should_return_en_existing_truck_by_id() {
        //given
        Long id = 32L;
        TruckEntity entity = new TruckEntity(id, "TEST123", null, null, null);
        Mockito.when(truckRepository.findById(id)).thenReturn(Optional.of(entity));

        //when
        TruckEntity result = truckRestService.getTruckById(id);

        //then
        assertEquals(entity, result);
        Mockito.verify(truckRepository).findById(id);
    }

    @Test
    void getTruckByPlates_should_throw_an_exception_when_there_is_no_truck_with_such_plates() {
        //given
        String plates = "NO123456";
        Mockito.when(truckRepository.findByTruckPlates(plates)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckRestService.getTruckByPlates(plates));
    }

    @Test
    void getTruckByPlates_should_return_en_existing_truck_by_plates() {
        //given
        String plates = "NO123456";
        TruckEntity entity = new TruckEntity(1L, plates, null, null, null);
        Mockito.when(truckRepository.findByTruckPlates(plates)).thenReturn(Optional.of(entity));

        //when
        TruckEntity result = truckRestService.getTruckByPlates(plates);

        //then
        assertEquals(entity, result);
        Mockito.verify(truckRepository).findByTruckPlates(plates);
    }

    @Test
    void addNewTruck_should_throw_an_exception_when_truck_witch_provided_plates_already_exists() {
        //given
        Long carrierId = 12L;
        String plates = "TEST123";
        TruckEntity entity = new TruckEntity(1L, plates, null, null, null);
        TruckNewUpdateDTO dto = new TruckNewUpdateDTO(plates);
        Mockito.when(truckRepository.findByTruckPlates(plates)).thenReturn(Optional.of(entity));

        //when + then
        assertThrows(IllegalOperationException.class, () -> truckRestService.addNewTruck(carrierId, dto));
        Mockito.verify(truckRepository).findByTruckPlates(plates);
        Mockito.verify(truckRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void addNewTruck_should_throw_an_exception_when_adding_truck_to_non_existing_carrier() {
        //given
        Long carrierId = 12L;
        String plates = "TEST123";
        TruckNewUpdateDTO dto = new TruckNewUpdateDTO(plates);
        Mockito.when(truckRepository.findByTruckPlates(plates)).thenReturn(Optional.empty());
        Mockito.when(carrierRepository.findById(carrierId)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckRestService.addNewTruck(carrierId, dto));
        Mockito.verify(truckRepository).findByTruckPlates(plates);
        Mockito.verify(carrierRepository).findById(carrierId);
        Mockito.verify(truckRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteTruck_Should_throw_an_exception_when_trying_to_delete_non_existing_truck() {
        //given
        String plates = "TEST4321";
        Mockito.when(truckRepository.findByTruckPlates(plates)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckRestService.deleteTruckByPlates(plates));
        Mockito.verify(truckRepository, Mockito.never()).delete(Mockito.any());
        ;
    }

    @Test
    void deleteTruck_should_delete_existing_truck() {
        //given
        String plates = "TEST4321";
        TruckEntity entity = new TruckEntity(1L, plates, null, null, null);
        Mockito.when(truckRepository.findByTruckPlates(plates)).thenReturn(Optional.of(entity));

        //when
        TruckEntity result = truckRestService.deleteTruckByPlates(plates);

        //then
        assertEquals(entity, result);
        Mockito.verify(truckRepository).findByTruckPlates(plates);
        Mockito.verify(truckRepository).delete(entity);
    }

    @Test
    void updateTruck_should_throw_an_exceptipn_if_non_existing_id_provided() {
        //given
        String plates = "TEST1234";
        TruckNewUpdateDTO dto = new TruckNewUpdateDTO("LPO9078");
        Mockito.when(truckRepository.findByTruckPlates(plates)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckRestService.updateTruck(plates, dto));
        Mockito.verify(truckRepository).findByTruckPlates(plates);
        Mockito.verify(truckRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateTruck_should_throw_an_exception_if_tryingg_to_update_with_existing_plates() {
        //given
        String plates = "TEST1234";
        TruckEntity entity = new TruckEntity(1L, plates, null, null, null);
        TruckNewUpdateDTO dto = new TruckNewUpdateDTO("LPO9078");
        Mockito.when(truckRepository.findByTruckPlates(plates)).thenReturn(Optional.of(entity));
        Mockito.when(truckRepository.findByTruckPlates(dto.getTruckPlates())).thenReturn(Optional.of(new TruckEntity()));
        Mockito.when(transformer.newUpdateToEntity(dto)).thenReturn(new TruckEntity(dto.getTruckPlates(), null, null, null));

        //when + then
        assertThrows(IllegalOperationException.class, () -> truckRestService.updateTruck(plates, dto));
        Mockito.verify(truckRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(truckRepository).findByTruckPlates(plates);
        Mockito.verify(truckRepository).findByTruckPlates(dto.getTruckPlates());
    }

    @Test
    void updateTruck_should_update_truck_by_existing_id() {
        String plates = "EXIST1234";
        TruckNewUpdateDTO dto = new TruckNewUpdateDTO("LPO9078");
        TruckEntity exist1234 = new TruckEntity(13L, "EXIST1234", null, null, null);
        TruckEntity dtoEntity = new TruckEntity(null, "LPO9078", null, null, null);
        TruckEntity savedEntity = new TruckEntity(13L, "LPO9078", null, null, null);

        Mockito.when(truckRepository.findByTruckPlates(plates)).thenReturn(Optional.of(exist1234));
        Mockito.when(transformer.newUpdateToEntity(dto)).thenReturn(dtoEntity);
        Mockito.when(truckRepository.findByTruckPlates(dtoEntity.getTruckPlates())).thenReturn(Optional.empty());
        Mockito.when(truckRepository.save(exist1234)).thenReturn(savedEntity);
        Mockito.when(transformer.entityToNewUpdateDto(savedEntity)).thenReturn(new TruckNewUpdateDTO("LPO9078"));

        //when
        TruckNewUpdateDTO result = truckRestService.updateTruck(plates, dto);

        //then
        assertEquals("LPO9078", result.getTruckPlates());
        Mockito.verify(truckRepository).findByTruckPlates(plates);
        Mockito.verify(transformer).newUpdateToEntity(dto);
        Mockito.verify(truckRepository).findByTruckPlates(dto.getTruckPlates());
        Mockito.verify(truckRepository).save(exist1234);
        Mockito.verify(transformer).entityToNewUpdateDto(savedEntity);
    }

}