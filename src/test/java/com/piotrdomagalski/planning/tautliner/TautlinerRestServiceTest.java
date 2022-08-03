package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TautlinerRestServiceTest {

    @TestConfiguration
    static class ReservationTestConfig {

        @Bean
        TautlinerRestService tautlinerRestService(TautlinerRepository tautlinerRepository, CarrierRepository carrierRepository,
                                                  TautlinerTransformer transformer) {
            return new TautlinerRestService(tautlinerRepository, carrierRepository, transformer);
        }
    }

    @MockBean
    TautlinerRepository tautlinerRepository;

    @MockBean
    CarrierRepository carrierRepository;

    @MockBean
    TautlinerTransformer transformer;

    @Autowired
    TautlinerRestService tautlinerRestService;

    @Test
    void getAllTautliners_should_return_an_empty_list_if_no_tatuliners_found() {
        //whem
        List<TautlinerEntity> result = tautlinerRestService.getAllTautliners();

        //then
        assertEquals(Collections.emptyList(), result);
        Mockito.verify(tautlinerRepository).findAll(Sort.by(Sort.Direction.ASC, "tautlinerPlates"));
    }

    @Test
    void getTautlinerByPlates_should_throw_an_exception_if_no_such_value_present() {

        //when + then
        assertThrows(NoSuchElementException.class, () -> tautlinerRestService.getTautlinerByPlates("ABC1234"));
    }

    @Test
    void getTautlinerByPlates_should_return_value_found_by_plates() {
        //given
        String plates = "ABC4321";
        TautlinerEntity tautlinerEntity = new TautlinerEntity(1L, true, "APL1234", null, null, null);
        Mockito.when(tautlinerRepository.findByTautlinerPlates(plates)).thenReturn(Optional.of(tautlinerEntity));

        //when
        TautlinerEntity result = tautlinerRestService.getTautlinerByPlates(plates);

        //then
        assertEquals(tautlinerEntity, result);
        Mockito.verify(tautlinerRepository).findByTautlinerPlates(plates);
    }

    @Test
    void getTautlinerById_should_throw_an_exception_if_no_such_value_present() {

        //when + then
        assertThrows(NoSuchElementException.class, () -> tautlinerRestService.getTautlinerById(32L));
    }

    @Test
    void getTautlinerById_should_return_value_found_by_id() {
        //given
        Long id = 543L;
        TautlinerEntity tautlinerEntity = new TautlinerEntity(543L, true, "APL1234", null, null, null);
        Mockito.when(tautlinerRepository.findById(id)).thenReturn(Optional.of(tautlinerEntity));

        //when
        TautlinerEntity result = tautlinerRestService.getTautlinerById(id);

        //then
        assertEquals(tautlinerEntity, result);
        Mockito.verify(tautlinerRepository).findById(id);
    }

    @Test
    void addNewTautliner_should_throw_an_exception_if_adding_existing_truck_plates() {
        //given
        TautlinerNewUpdateDTO tautliner = new TautlinerNewUpdateDTO(true, "ABC1234", "10-10-2022");
        TautlinerEntity tautlinerEntity = new TautlinerEntity(1L, true, "ABC1234",
                LocalDateTime.of(2022, 10, 10, 0, 0, 0), null, null);
        Mockito.when(tautlinerRepository.findByTautlinerPlates(tautliner.getTautlinerPlates())).thenReturn(Optional.of(tautlinerEntity));

        //when + then
        assertThrows(IllegalOperationException.class, () -> tautlinerRestService.addNewTautliner(12L, tautliner));
    }

    @Test
    void addNewTautliner_should_throw_an_exception_when_adding_no_xpo_tautliner_with_null_carrier() {
        //given
        TautlinerNewUpdateDTO tautliner = new TautlinerNewUpdateDTO(false, "ABC1234", "10-10-2022");

        //when + then
        assertThrows(IllegalOperationException.class, () -> tautlinerRestService.addNewTautliner(null, tautliner));
    }

    @Test
    void addNewTautliner_should_throw_an_exception_when_trying_to_add_to_non_existing_carrier() {
        //given
        Long carrierId = 432L;
        TautlinerNewUpdateDTO tautlinerUpdate = new TautlinerNewUpdateDTO(true, "ABC1234", "10-10-2022");
        Mockito.when(tautlinerRepository.findByTautlinerPlates(tautlinerUpdate.getTautlinerPlates())).thenReturn(Optional.empty());
        Mockito.when(carrierRepository.findById(carrierId)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> tautlinerRestService.addNewTautliner(carrierId, tautlinerUpdate));
        Mockito.verify(tautlinerRepository, Mockito.never()).save(Mockito.any());
    }

    @ParameterizedTest
    @ArgumentsSource(TautlinerAddArgumentsProvider.class)
    void addNewTautliner_should_add_tautliner_with_no_carrier_if_carrier_was_null(TautlinerNewUpdateDTO input, TautlinerEntity entity) {
        //given
        Mockito.when(tautlinerRepository.findByTautlinerPlates(input.getTautlinerPlates())).thenReturn(Optional.empty());
        Mockito.when(transformer.newUpdateDTOtoEntity(input)).thenReturn(entity);
        Mockito.when(tautlinerRepository.save(entity)).thenReturn(entity);

        //when
        TautlinerEntity result = tautlinerRestService.addNewTautliner(null, input);

        //then
        assertEquals(entity, result);
        assertNull(entity.getCarrier());
        Mockito.verify(tautlinerRepository).findByTautlinerPlates(input.getTautlinerPlates());
        Mockito.verify(transformer).newUpdateDTOtoEntity(input);
        Mockito.verify(tautlinerRepository).save(entity);
    }


    @ParameterizedTest
    @ArgumentsSource(TautlinerAddArgumentsProvider.class)
    void addNewTautliner_should_add_new_tatuliner_with_carrier(TautlinerNewUpdateDTO input, TautlinerEntity entity, CarrierEntity carrier) {
        //given
        Mockito.when(tautlinerRepository.findByTautlinerPlates(input.getTautlinerPlates())).thenReturn(Optional.empty());
        Mockito.when(transformer.newUpdateDTOtoEntity(input)).thenReturn(entity);
        Mockito.when(carrierRepository.findById(carrier.getId())).thenReturn(Optional.of(carrier));
        Mockito.when(tautlinerRepository.save(entity)).thenReturn(entity);

        //when
        TautlinerEntity result = tautlinerRestService.addNewTautliner(carrier.getId(), input);

        //then
        assertEquals(entity, result);
        assertEquals(carrier.getId(), result.getCarrier().getId());
        assertEquals(result.getTautlinerPlates(), result.getCarrier().getTautliners().get(0).getTautlinerPlates());
        Mockito.verify(tautlinerRepository).findByTautlinerPlates(input.getTautlinerPlates());
        Mockito.verify(transformer).newUpdateDTOtoEntity(input);
        Mockito.verify(tautlinerRepository).save(entity);
    }

    @Test
    void deleteTautlinerByPlates_should_throw_an_exception_if_no_record_found() {
        //given
        String plates = "TRE132";
        Mockito.when(tautlinerRepository.findByTautlinerPlates(plates)).thenReturn(Optional.empty());

        //when+then
        assertThrows(NoSuchElementException.class, () -> tautlinerRestService.deleteTautlinerByPlates(plates));
    }

    @Test
    void deleteTautlinerByPlates_should_delete_recond_when_correct_plates_provided() {
        //given
        String plates = "TRE132";
        TautlinerEntity entity = new TautlinerEntity(1L, true, "TRE132",
                LocalDateTime.of(2022, 10, 10, 0, 0, 0), null, null);
        Mockito.when(tautlinerRepository.findByTautlinerPlates(plates)).thenReturn(Optional.of(entity));

        //when
        TautlinerEntity result = tautlinerRestService.deleteTautlinerByPlates(plates);

        //then
        assertEquals(entity, result);
        Mockito.verify(tautlinerRepository).findByTautlinerPlates(plates);
        Mockito.verify(tautlinerRepository).delete(entity);
    }

    @Test
    void updateTautlinerByPlates_should_throw_an_exception_when_trying_to_updane_non_existing_record() {
        //given
        String plates = "PO12345";
        TautlinerNewUpdateDTO update = new TautlinerNewUpdateDTO(false, null, null);

        //when + then
        assertThrows(NoSuchElementException.class, () -> tautlinerRestService.updateTautlinerByPlates(plates, update));
    }

    @ParameterizedTest
    @ArgumentsSource(TautlinerUpdateTautlinerArgumentsProvider.class)
    void updateTautlinerByPlates_should_update_existing_tautliner(String plates, TautlinerEntity foundById,
                                                                  TautlinerNewUpdateDTO provided,
                                                                  TautlinerEntity translatedToEntity,
                                                                  TautlinerNewUpdateDTO expected) {

        //given
        Mockito.when(tautlinerRepository.findByTautlinerPlates(plates)).thenReturn(Optional.of(foundById));
        Mockito.when(transformer.newUpdateDTOtoEntity(provided)).thenReturn(translatedToEntity);
        Mockito.when(tautlinerRepository.save(foundById)).thenReturn(foundById);
        Mockito.when(transformer.entityToNewUpdateDTO(foundById)).thenReturn(expected);

        //when
        TautlinerNewUpdateDTO result = tautlinerRestService.updateTautlinerByPlates(plates, provided);

        //then
        assertEquals(expected, result);
        Mockito.verify(tautlinerRepository).findByTautlinerPlates(plates);
        Mockito.verify(transformer).newUpdateDTOtoEntity(provided);
        Mockito.verify(tautlinerRepository).save(foundById);
        Mockito.verify(transformer).entityToNewUpdateDTO(foundById);

    }


}