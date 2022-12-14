package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.tautliner.TautlinerRepository;
import com.piotrdomagalski.planning.truck.TruckRepository;
import com.piotrdomagalski.planning.truck_driver.TruckDriverRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class CarrierRestServiceTest {

    @Autowired
    CarrierService carrierService;
    @MockBean
    CarrierRepository carrierRepository;
    @MockBean
    CarrierActions carrierOperations;
    @MockBean
    CarrierTransformer transformer;
    @MockBean
    TruckRepository truckRepository;
    @MockBean
    TruckDriverRepository truckDriverRepository;
    @MockBean
    TautlinerRepository tautlinerRepository;

    @Test
    void getAllCarriers_should_return_an_empty_list_when_no_carriers_are_present() {
        //when
        Page<CarrierFullIDto> result = carrierService.getAllCarriers(0, 10);

        //then
        assertEquals(Page.empty(), result);
    }

    @Test
    void getCarriersShortInfo_should_return_an_empty_page_when_no_carriers_are_present() {
        //when
        Page<CarrierShortInfoDTO> result = carrierService.getCarriersShortInfo(0, 10);

        //then
        assertEquals(Page.empty(), result);
    }

    @Test
    void getCarrierById_should_throw_an_exception_when_there_is_no_carrier_with_such_id() {
        //given
        Long id = 66L;
        Mockito.when(carrierRepository.findById(id)).thenReturn(Optional.empty());
        //when + then
        assertThrows(NoSuchElementException.class, () -> carrierService.getCarrierById(id));
    }

    @Test
    void getCarrierById_should_return_en_existing_carrier_by_id() {
        //given
        Long id = 48956L;
        CarrierEntity carrier = new CarrierEntity(id, "123456", "Test carrier", "Carrierland", 1.2,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        CarrierFullIDto dto = new CarrierFullIDto("123456", "Test carrier", "Carrierland", 1.2,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Mockito.when(carrierRepository.findById(id)).thenReturn(Optional.of(carrier));
        Mockito.when(transformer.toCarrierFullIDto(carrier)).thenReturn(dto);

        //when
        CarrierFullIDto result = carrierService.getCarrierById(id);

        //then
        assertEquals(dto, result);
        Mockito.verify(carrierRepository).findById(id);
    }

    @Test
    void getCarrierBySap_should_throw_an_exception_when_there_is_no_carrier_with_such_sap() {
        //given
        String sap = "654456";
        Mockito.when(carrierRepository.findBySap(sap)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> carrierService.getCarrierBySap(sap));
    }

    @Test
    void getCarrierBySap_should_return_en_existing_carrier_by_sap() {
        //given
        String sap = "654456";
        CarrierEntity carrier = new CarrierEntity(12L, "654456", "Test carrier", "Carrierland", 1.2,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        CarrierFullIDto dto = new CarrierFullIDto("654456", "Test carrier", "Carrierland", 1.2,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Mockito.when(carrierRepository.findBySap(sap)).thenReturn(Optional.of(carrier));
        Mockito.when(transformer.toCarrierFullIDto(carrier)).thenReturn(dto);

        //when
        CarrierFullIDto result = carrierService.getCarrierBySap(sap);

        //then
        assertEquals(dto, result);
        Mockito.verify(carrierRepository).findBySap(sap);
    }

    @Test
    void addNewCarrier_should_throw_an_exception_when_carrier_witch_provided_sap_already_exists() {
        //given
        String sap = "963123";
        CarrierEntity carrier = new CarrierEntity(465L, sap, "Test carrier", "Carrierland", 1.2,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        CarrierNewUpdateDTO newCarrierDto = new CarrierNewUpdateDTO(sap, "Other carrier", "otherland", 1.1);
        Mockito.when(carrierRepository.findBySap(sap)).thenReturn(Optional.of(carrier));

        //when + then
        assertThrows(IllegalOperationException.class, () -> carrierService.addNewCarrier(newCarrierDto));
        Mockito.verify(carrierRepository).findBySap(sap);
        Mockito.verify(carrierRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteCarrierBySap_should_throw_an_exception_when_trying_to_delete_non_existing_carrier() {
        //given
        String sap = "963123";
        Mockito.when(carrierRepository.findBySap(sap)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> carrierService.deleteCarrierBySap(sap));
        Mockito.verify(carrierRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteCarrierBySap_should_delete_existing_carrier() {
        //given
        String sap = "963123";
        CarrierEntity carrier = new CarrierEntity(12L, "963123", "Test carrier", "Carrierland", 1.2,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        CarrierShortInfoDTO dto = new CarrierShortInfoDTO("963123", "Test carrier", "Carrierland", 1.2, 0, 0);
        Mockito.when(carrierRepository.findBySap(sap)).thenReturn(Optional.of(carrier));
        Mockito.when(transformer.entityToShortInfoDto(carrier)).thenReturn(dto);
        Mockito.when(carrierOperations.clear(carrier)).thenReturn(true);

        //when
        CarrierShortInfoDTO result = carrierService.deleteCarrierBySap(sap);

        //then
        assertEquals(dto, result);
        Mockito.verify(carrierRepository).findBySap(sap);
        Mockito.verify(carrierRepository).delete(carrier);
        Mockito.verify(carrierOperations).clear(carrier);
    }

    @Test
    void updateCarrier_should_throw_an_exceptipn_if_non_existing_sap_provided() {
        //given
        String sap = "963123";
        CarrierNewUpdateDTO updateCarrierDto = new CarrierNewUpdateDTO(null, "Other carrier", null, null);
        Mockito.when(carrierRepository.findBySap(sap)).thenReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> carrierService.updateCarrier(sap, updateCarrierDto));
        Mockito.verify(carrierRepository).findBySap(sap);
        Mockito.verify(carrierRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateCarrier_should_throw_an_exception_if_trying_to_update_with_existing_sap() {
        //given
        String sap = "963123";
        CarrierEntity carrier = new CarrierEntity(465L, sap, "Test carrier", "Carrierland", 1.2,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        CarrierNewUpdateDTO updateCarrierDto = new CarrierNewUpdateDTO("777777", null, null, null);
        Mockito.when(carrierRepository.findBySap(sap)).thenReturn(Optional.of(carrier));
        Mockito.when(carrierRepository.findBySap(updateCarrierDto.getSap())).thenReturn(Optional.of(new CarrierEntity()));
        Mockito.when(transformer.newUpdateToEntity(updateCarrierDto)).thenReturn(new CarrierEntity(null, "777777", null, null, null, null, null, null));

        //when + then
        assertThrows(IllegalOperationException.class, () -> carrierService.updateCarrier(sap, updateCarrierDto));
        Mockito.verify(carrierRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(carrierRepository).findBySap(sap);
        Mockito.verify(carrierRepository).findBySap(updateCarrierDto.getSap());
    }

    @ParameterizedTest
    @ArgumentsSource(CarrierUpdateArgumentsProvider.class)
    void updateCarrier_should_update_carrier_by_existing_sap(String sap, CarrierNewUpdateDTO dto,
                                                             CarrierEntity foundBySap, CarrierEntity dtoToEntity,
                                                             CarrierEntity savedCarrier) {
        //given
        Mockito.when(carrierRepository.findBySap(sap)).thenReturn(Optional.of(foundBySap));
        Mockito.when(transformer.newUpdateToEntity(dto)).thenReturn(dtoToEntity);
        Mockito.when(carrierRepository.findBySap(dtoToEntity.getSap())).thenReturn(Optional.empty());
        Mockito.when(carrierRepository.save(foundBySap)).thenReturn(savedCarrier);
        Mockito.when(transformer.entityToNewUpdateDto(savedCarrier)).thenReturn(new CarrierNewUpdateDTO(savedCarrier.getSap(), savedCarrier.getName(), savedCarrier.getOrigin(), savedCarrier.getRate()));

        //when
        CarrierNewUpdateDTO result = carrierService.updateCarrier(sap, dto);

        //then
        assertEquals(savedCarrier.getSap(), result.getSap());
        Mockito.verify(carrierRepository).findBySap(sap);
        Mockito.verify(transformer).newUpdateToEntity(dto);
        Mockito.verify(carrierRepository).save(foundBySap);
        Mockito.verify(transformer).entityToNewUpdateDto(savedCarrier);

    }

    @TestConfiguration
    static class CarrierServiceTestingConfiguration {

        @Bean
        CarrierService carrierRestService(CarrierRepository carrierRepository, CarrierTransformer transformer, CarrierActions carrierOperations,
                                          TruckRepository truckRepository, TruckDriverRepository driverRepository, TautlinerRepository tautlinerRepository) {
            return new CarrierService(carrierRepository, transformer, carrierOperations, truckRepository, driverRepository, tautlinerRepository);
        }
    }
}