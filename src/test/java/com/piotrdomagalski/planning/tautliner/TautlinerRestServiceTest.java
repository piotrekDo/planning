package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.carrier.CarrierActions;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.logs.LogsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TautlinerRestServiceTest {

    @MockBean
    CarrierActions carrierOperations;
    @MockBean
    TautlinerRepository tautlinerRepository;
    @MockBean
    CarrierRepository carrierRepository;
    @MockBean
    TautlinerTransformer transformer;
    @MockBean
    LogsService logsService;

    @Autowired
    TautlinerService tautlinerRestService;

    @Test
    void getAllTautliners_should_return_an_empty_list_if_no_tatuliners_found() {
        //when
        Page<TautlinerInfoDTO> result = tautlinerRestService.getAllTautliners(null, 0, 10);

        //then
        assertEquals(Page.empty(), result);
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
        TautlinerInfoDTO tautlinerInfoDTO = new TautlinerInfoDTO(tautlinerEntity.getTautlinerPlates(), tautlinerEntity.getTechInspection(), tautlinerEntity.getXpo(), null, null, null);
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(plates)).thenReturn(Optional.of(tautlinerEntity));
        Mockito.when(transformer.entityToInfoDTO(tautlinerEntity)).thenReturn(tautlinerInfoDTO);

        //when
        TautlinerInfoDTO result = tautlinerRestService.getTautlinerByPlates(plates);

        //then
        assertEquals(tautlinerInfoDTO, result);
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(plates);
        Mockito.verify(transformer).entityToInfoDTO(tautlinerEntity);
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
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(tautliner.getTautlinerPlates())).thenReturn(Optional.of(tautlinerEntity));

        //when + then
        assertThrows(IllegalOperationException.class, () -> tautlinerRestService.addNewTautliner("123456", tautliner));
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
        String carrierSap = "123456";
        TautlinerNewUpdateDTO tautlinerUpdate = new TautlinerNewUpdateDTO(true, "ABC1234", "10-10-2022");
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(tautlinerUpdate.getTautlinerPlates())).thenReturn(Optional.empty());
        Mockito.when(carrierRepository.findBySap(carrierSap)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> tautlinerRestService.addNewTautliner(carrierSap, tautlinerUpdate));
        Mockito.verify(tautlinerRepository, Mockito.never()).save(Mockito.any());
    }

    @ParameterizedTest
    @ArgumentsSource(TautlinerAddArgumentsProvider.class)
    void addNewTautliner_should_add_tautliner_with_no_carrier_if_carrier_was_null(TautlinerNewUpdateDTO input, TautlinerEntity entity,
                                                                                  CarrierEntity carrier, TautlinerInfoDTO tautlinerInfoDTO) {
        //given
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(input.getTautlinerPlates())).thenReturn(Optional.empty());
        Mockito.when(transformer.newUpdateDTOtoEntity(input)).thenReturn(entity);
        Mockito.when(tautlinerRepository.save(entity)).thenReturn(entity);
        Mockito.when(transformer.entityToInfoDTO(entity)).thenReturn(tautlinerInfoDTO);

        //when
        TautlinerInfoDTO result = tautlinerRestService.addNewTautliner(null, input);

        //then
        assertEquals(tautlinerInfoDTO, result);
        assertNull(entity.getCarrier());
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(input.getTautlinerPlates());
        Mockito.verify(transformer).newUpdateDTOtoEntity(input);
        Mockito.verify(tautlinerRepository).save(entity);
    }

    @ParameterizedTest
    @ValueSource(strings = {"das", "12 2345", "ds434", "12BNSA"})
    void addNewTautliner_should_throw_an_exception_when_provided_carrierSap_is_not_valid(String carrierSap) {
        //given
        TautlinerNewUpdateDTO tautlinerUpdate = new TautlinerNewUpdateDTO(true, "ABC1234", "10-10-2022");
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(tautlinerUpdate.getTautlinerPlates())).thenReturn(Optional.empty());

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> tautlinerRestService.addNewTautliner(carrierSap, tautlinerUpdate));
        assertEquals(carrierSap + " is not a valid SAP number", exception.getMessage());
        Mockito.verify(tautlinerRepository, Mockito.never()).save(Mockito.any());
    }

    @ParameterizedTest
    @ArgumentsSource(TautlinerAddArgumentsProvider.class)
    void addNewTautliner_should_add_new_tatuliner_with_carrier(TautlinerNewUpdateDTO input, TautlinerEntity entity,
                                                               CarrierEntity carrier, TautlinerInfoDTO tautlinerInfoDTO) {
        //given
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(input.getTautlinerPlates())).thenReturn(Optional.empty());
        Mockito.when(transformer.newUpdateDTOtoEntity(input)).thenReturn(entity);
        Mockito.when(carrierRepository.findBySap(carrier.getSap())).thenReturn(Optional.of(carrier));
        Mockito.when(tautlinerRepository.save(entity)).thenReturn(entity);
        Mockito.when(transformer.entityToInfoDTO(entity)).thenReturn(tautlinerInfoDTO);
        Mockito.when(carrierOperations.addTautliner(carrier, entity)).thenReturn(true);

        //when
        TautlinerInfoDTO result = tautlinerRestService.addNewTautliner(carrier.getSap(), input);

        //then
        assertEquals(tautlinerInfoDTO, result);
        assertEquals(carrier.getSap(), result.getCarrierSap());
        assertEquals(entity.getTautlinerPlates(), result.getTautlinerPlates());
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(input.getTautlinerPlates());
        Mockito.verify(transformer).newUpdateDTOtoEntity(input);
        Mockito.verify(tautlinerRepository).save(entity);
        Mockito.verify(transformer).entityToInfoDTO(entity);
        Mockito.verify(carrierOperations).addTautliner(carrier, entity);
    }

    @Test
    void deleteTautlinerByPlates_should_throw_an_exception_if_no_record_found() {
        //given
        String plates = "TRE132";
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(plates)).thenReturn(Optional.empty());

        //when+then
        assertThrows(NoSuchElementException.class, () -> tautlinerRestService.deleteTautlinerByPlates(plates));
    }

    @Test
    void deleteTautlinerByPlates_should_delete_recond_when_correct_plates_provided() {
        //given
        String plates = "TRE132";
        TautlinerEntity entity = new TautlinerEntity(1L, true, "TRE132",
                LocalDateTime.of(2022, 10, 10, 0, 0, 0), null, null);
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(plates)).thenReturn(Optional.of(entity));

        //when
        TautlinerEntity result = tautlinerRestService.deleteTautlinerByPlates(plates);

        //then
        assertEquals(entity, result);
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(plates);
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

    @Test
    void updateTautliner_should_throw_an_exception_if_trying_to_change_plates_to_exising_one() {
        //given
        String tautlinerPlates = "FOUND123";
        TautlinerNewUpdateDTO dto = new TautlinerNewUpdateDTO(false, "NEW12345", null);
        TautlinerEntity tautlinerFound = new TautlinerEntity(12L, true, tautlinerPlates, LocalDateTime.now(), null, null);
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(tautlinerPlates)).thenReturn(Optional.of(tautlinerFound));
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(dto.getTautlinerPlates())).thenReturn(Optional.of(new TautlinerEntity()));
        Mockito.when(transformer.newUpdateDTOtoEntity(dto)).thenReturn(new TautlinerEntity(false, "NEW12345", null, null, null));

        //when + then
        assertThrows(IllegalOperationException.class, () -> tautlinerRestService.updateTautlinerByPlates(tautlinerPlates, dto));
        Mockito.verify(tautlinerRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(tautlinerPlates);
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(dto.getTautlinerPlates());

    }

    @ParameterizedTest
    @ArgumentsSource(TautlinerUpdateTautlinerArgumentsProvider.class)
    void updateTautlinerByPlates_should_update_existing_tautliner(String plates, TautlinerEntity foundById,
                                                                  TautlinerNewUpdateDTO provided,
                                                                  TautlinerEntity translatedToEntity,
                                                                  TautlinerNewUpdateDTO expected) {

        //given
        Mockito.when(tautlinerRepository.findByTautlinerPlatesIgnoreCase(plates)).thenReturn(Optional.of(foundById));
        Mockito.when(transformer.newUpdateDTOtoEntity(provided)).thenReturn(translatedToEntity);
        Mockito.when(tautlinerRepository.save(foundById)).thenReturn(foundById);
        Mockito.when(transformer.entityToNewUpdateDTO(foundById)).thenReturn(expected);

        //when
        TautlinerNewUpdateDTO result = tautlinerRestService.updateTautlinerByPlates(plates, provided);

        //then
        assertEquals(expected, result);
        Mockito.verify(tautlinerRepository).findByTautlinerPlatesIgnoreCase(plates);
        Mockito.verify(transformer).newUpdateDTOtoEntity(provided);
        Mockito.verify(tautlinerRepository).save(foundById);
        Mockito.verify(transformer).entityToNewUpdateDTO(foundById);

    }

    @TestConfiguration
    static class TautlinersTestConfig {

        @Bean
        TautlinerService tautlinerRestService(TautlinerRepository tautlinerRepository, CarrierRepository carrierRepository,
                                              TautlinerTransformer transformer, CarrierActions carrierOperations, LogsService logsService) {
            return new TautlinerService(tautlinerRepository, carrierRepository, transformer, carrierOperations, logsService);
        }
    }

}