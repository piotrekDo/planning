package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierActions;
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
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TruckRestServiceTest {

    @TestConfiguration
    static class TruckServiceTestConfig {

        @Bean
        TruckService truckRestService(TruckRepository truckRepository, CarrierRepository carrierRepository,
                                      TruckTransformer transformer, CarrierActions carrierOperations) {
            return new TruckService(truckRepository, carrierRepository, transformer, carrierOperations);
        }
    }

    @Autowired
    TruckService truckRestService;

    @MockBean
    CarrierActions carrierOperations;

    @MockBean
    TruckTransformer transformer;

    @MockBean
    TruckRepository truckRepository;

    @MockBean
    CarrierRepository carrierRepository;

    @Test
    void getAllTrucks_should_return_an_empty_list_when_no_trucks_present() {
        //when
        Page<TruckInfoDTO> result = truckRestService.getAllTrucks(0, 10);

        //then
        assertEquals(Page.empty(), result);
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
        TruckEntity entity = new TruckEntity(id, "TEST123", false, null, null, null);
        TruckInfoDTO dto = new TruckInfoDTO("TEST123", false, null, null, null, null, null, null, null, null, null);
        Mockito.when(truckRepository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.when(transformer.toinfoDto(entity)).thenReturn(dto);

        //when
        TruckInfoDTO result = truckRestService.getTruckById(id);

        //then
        assertEquals(dto, result);
        Mockito.verify(truckRepository).findById(id);
    }

    @Test
    void getTruckByPlates_should_throw_an_exception_when_there_is_no_truck_with_such_plates() {
        //given
        String plates = "NO123456";
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(plates)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckRestService.getTruckByPlates(plates));
    }

    @Test
    void getTruckByPlates_should_return_en_existing_truck_by_plates() {
        //given
        String plates = "NO123456";
        TruckEntity entity = new TruckEntity(1L, plates, false, null, null, null);
        TruckInfoDTO dto = new TruckInfoDTO(plates, false, null, null, null, null, null, null, null, null, null);
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(plates)).thenReturn(Optional.of(entity));
        Mockito.when(transformer.toinfoDto(entity)).thenReturn(dto);

        //when
        TruckInfoDTO result = truckRestService.getTruckByPlates(plates);

        //then
        assertEquals(dto, result);
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(plates);
    }

    @Test
    void addNewTruck_should_throw_an_exception_when_truck_witch_provided_plates_already_exists() {
        //given
        String carrierSap = "123456";
        String plates = "TEST123";
        TruckEntity entity = new TruckEntity(1L, plates, true, null, null, null);
        TruckNewUpdateDTO dto = new TruckNewUpdateDTO(plates, true);
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(plates)).thenReturn(Optional.of(entity));

        //when + then
        assertThrows(IllegalOperationException.class, () -> truckRestService.addNewTruck(carrierSap, dto));
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(plates);
        Mockito.verify(truckRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void addNewTruck_should_throw_an_exception_when_adding_truck_to_non_existing_carrier() {
        //given
        String carrierSap = "123456";
        String plates = "TEST123";
        TruckNewUpdateDTO dto = new TruckNewUpdateDTO(plates, true);
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(plates)).thenReturn(Optional.empty());
        Mockito.when(carrierRepository.findBySap(carrierSap)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckRestService.addNewTruck(carrierSap, dto));
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(plates);
        Mockito.verify(carrierRepository).findBySap(carrierSap);
        Mockito.verify(truckRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteTruck_Should_throw_an_exception_when_trying_to_delete_non_existing_truck() {
        //given
        String plates = "TEST4321";
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(plates)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckRestService.deleteTruckByPlates(plates));
        Mockito.verify(truckRepository, Mockito.never()).delete(Mockito.any());
        ;
    }

    @Test
    void deleteTruck_should_delete_existing_truck() {
        //given
        String plates = "TEST4321";
        TruckEntity entity = new TruckEntity(1L, plates, null, null, null, null);
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(plates)).thenReturn(Optional.of(entity));

        //when
        TruckEntity result = truckRestService.deleteTruckByPlates(plates);

        //then
        assertEquals(entity, result);
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(plates);
        Mockito.verify(truckRepository).delete(entity);
    }

    @Test
    void updateTruck_should_throw_an_exceptipn_if_non_existing_id_provided() {
        //given
        String plates = "TEST1234";
        TruckNewUpdateDTO dto = new TruckNewUpdateDTO("LPO9078", null);
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(plates)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> truckRestService.updateTruck(plates, dto));
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(plates);
        Mockito.verify(truckRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateTruck_should_throw_an_exception_if_tryingg_to_update_with_existing_plates() {
        //given
        String plates = "TEST1234";
        TruckEntity entity = new TruckEntity(1L, plates, true, null, null, null);
        TruckNewUpdateDTO dto = new TruckNewUpdateDTO("LPO9078", null);
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(plates)).thenReturn(Optional.of(entity));
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(dto.getTruckPlates())).thenReturn(Optional.of(new TruckEntity()));
        Mockito.when(transformer.newUpdateToEntity(dto)).thenReturn(new TruckEntity(dto.getTruckPlates(), dto.getMega(), null, null, null));

        //when + then
        assertThrows(IllegalOperationException.class, () -> truckRestService.updateTruck(plates, dto));
        Mockito.verify(truckRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(plates);
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(dto.getTruckPlates());
    }

    @ParameterizedTest
    @ArgumentsSource(TruckUpdateArgumentsProvider.class)
    void updateTruck_should_update_truck_by_existing_id(String plates, TruckNewUpdateDTO dto,
                                                        TruckEntity foundByPlates, TruckEntity dtoTranslated,
                                                        TruckEntity savedEntity) {
        //given
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(plates)).thenReturn(Optional.of(foundByPlates));
        Mockito.when(transformer.newUpdateToEntity(dto)).thenReturn(dtoTranslated);
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(dtoTranslated.getTruckPlates())).thenReturn(Optional.empty());
        Mockito.when(truckRepository.save(foundByPlates)).thenReturn(savedEntity);
        Mockito.when(transformer.entityToNewUpdateDto(savedEntity)).thenReturn(new TruckNewUpdateDTO(savedEntity.getTruckPlates(), savedEntity.getMega()));

        //when
        TruckNewUpdateDTO result = truckRestService.updateTruck(plates, dto);

        //then
        assertEquals(savedEntity.getTruckPlates(), result.getTruckPlates());
        Mockito.verify(truckRepository).findByTruckPlatesIgnoreCase(plates);
        Mockito.verify(transformer).newUpdateToEntity(dto);
        Mockito.verify(truckRepository).save(foundByPlates);
        Mockito.verify(transformer).entityToNewUpdateDto(savedEntity);
    }

}