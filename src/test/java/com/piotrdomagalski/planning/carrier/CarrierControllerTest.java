package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.tautliner.TautlinerInfoDTO;
import com.piotrdomagalski.planning.truck.TruckInfoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarrierController.class)
class CarrierControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CarrierRestService carrierRestService;

    @Test
    void getCarrierBySap_should_return_code_200_and_response_body_when_carrier_found() throws Exception {
        //given
        String sap = "123456";
        CarrierFullIDto carrier = new CarrierFullIDto(sap, "Test Carrier", "Carrierland", 1.2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TruckInfoDTO truck = new TruckInfoDTO("TRUCK1234", true, sap, carrier.getName(), null, null, null, null, null, null, null);
        TautlinerInfoDTO tautliner = new TautlinerInfoDTO("TAUT432", LocalDateTime.of(2022, 12, 24, 0, 0, 0), true, carrier.getName(), carrier.getSap(), null);
        carrier.getTrucks().add(truck);
        carrier.getTautliners().add(tautliner);
        Mockito.when(carrierRestService.getCarrierBySap(sap)).thenReturn(carrier);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/carriers/" + sap).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(carrierRestService).getCarrierBySap(sap);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Test Carrier")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sap", equalTo("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate", equalTo(1.2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautliners[0].carrierSap", equalTo("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautliners[0].tautlinerPlates", equalTo("TAUT432")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trucks[0].carrierSap", equalTo("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trucks[0].truckPlates", equalTo("TRUCK1234")));
    }

    @Test
    void getCarrierBySap_should_return_not_found_if_no_carrier_found() throws Exception {
        //given
        String sap = "123456";
        Mockito.when(carrierRestService.getCarrierBySap(sap)).thenThrow(
                new NoSuchElementException("No carrier found with id: " + sap));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/carriers/" + sap).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(carrierRestService).getCarrierBySap(sap);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier found with id: " + sap)));
    }

    @Test
    void addNewCarrier_should_return_code_200_and_response_body() throws Exception {
        //given
        CarrierNewUpdateDTO carrier = new CarrierNewUpdateDTO("123456", "Test Carrier", "Testland", 1.2);
        Mockito.when(carrierRestService.addNewCarrier(carrier)).thenReturn(carrier);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/carriers").contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                  "name": "Test Carrier",
                                  "origin": "Testland",
                                  "rate": 1.2,
                                  "sap": "123456"
                                }
                                """
                ));

        //then
        Mockito.verify(carrierRestService).addNewCarrier(carrier);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Test Carrier")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.origin", equalTo("Testland")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sap", equalTo("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate", equalTo(1.2)));
    }

    @Test
    void addNewCarrier_should_return_bad_request_if_carrier_with_provided_sap_already_exists() throws Exception {
        //given
        CarrierNewUpdateDTO carrier = new CarrierNewUpdateDTO("123456", "Test Carrier", "Testland", 1.2);
        Mockito.when(carrierRestService.addNewCarrier(carrier)).thenThrow(
                new IllegalOperationException(String.format("Carrier with SAP number: %s already exists!", carrier.getSap())));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/carriers").contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                  "name": "Test Carrier",
                                  "origin": "Testland",
                                  "rate": 1.2,
                                  "sap": "123456"
                                }
                                """
                ));

        //then
        Mockito.verify(carrierRestService).addNewCarrier(carrier);
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo(String.format("Carrier with SAP number: %s already exists!", carrier.getSap()))));
    }

    @ParameterizedTest
    @ArgumentsSource(ControllerAddNewCarrierArgumentsProvider.class)
    void addNewTruck_should_return_bad_request_when_adding_not_valid_entry(String newTruckJson, int code,
                                                                           String msg,
                                                                           String[] detailsSap, String[] detailsName,
                                                                           String[] detailsOrigin, String[] detailsRate) throws Exception {

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/carriers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTruckJson));
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(code)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(msg)));
        if (detailsSap != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.sap").value(containsInAnyOrder(detailsSap)));
        if (detailsName != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.name").value(containsInAnyOrder(detailsName)));
        if (detailsOrigin != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.origin").value(containsInAnyOrder(detailsOrigin)));
        if (detailsRate != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.rate").value(containsInAnyOrder(detailsRate)));
        Mockito.verify(carrierRestService, Mockito.never()).addNewCarrier(Mockito.any());
    }

    @Test
    void deleteCarrierBySap_should_return_code_200_when_deleting_by_existing_sap() throws Exception {
        //given
        String sap = "123456";
        CarrierShortInfoDTO carrier = new CarrierShortInfoDTO(sap, "Test Trans", "Testland", 1.2, 0, 0);
        Mockito.when(carrierRestService.deleteCarrierBySap(sap)).thenReturn(carrier);


        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/carriers/" + sap).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(carrierRestService).deleteCarrierBySap(sap);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sap", equalTo("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Test Trans")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.origin", equalTo("Testland")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trucks", equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.megas", equalTo(0)));
    }

    @Test
    void deleteCarrier_should_return_not_found_if_deleting_by_non_existing_sap() throws Exception {
        //given
        String sap = "123456";
        Mockito.when(carrierRestService.deleteCarrierBySap(sap)).thenThrow(
                new NoSuchElementException("No carrier found with id: " + sap)
        );

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/carriers/" + sap).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier found with id: " + sap)));
    }

    @ParameterizedTest
    @ArgumentsSource(CarrierControllerUpdateArugmentsProvider.class)
    void updateCarrier_should_reutn_conde_200_and_response_body_if_updated(CarrierNewUpdateDTO updateDto, String updateJson, CarrierNewUpdateDTO result) throws Exception {
        //given
        String sap = "123456";
        Mockito.when(carrierRestService.updateCarrier(sap, updateDto)).thenReturn(result);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/carriers/" + sap).contentType(MediaType.APPLICATION_JSON)
                .content(updateJson));

        //then
        Mockito.verify(carrierRestService).updateCarrier(sap, updateDto);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(result.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.origin", equalTo(result.getOrigin())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate", equalTo(result.getRate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sap", equalTo(result.getSap())));
    }

    @Test
    void updateCarrier_should_return_not_found_when_updating_by_non_existing_sap() throws Exception {
        //given
        String sap = "123456";
        CarrierNewUpdateDTO updateDTO = new CarrierNewUpdateDTO(null, "Lol Trans", "Testland", null);
        Mockito.when(carrierRestService.updateCarrier(sap, updateDTO)).thenThrow(
                new NoSuchElementException("No carrier found with id: " + sap));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/carriers/" + sap).contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                  "name": "Lol Trans",
                                  "origin": "Testland"
                                }
                                """
                ));
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier found with id: " + sap)));
    }

    @Test
    void updateCarrier_should_return_bad_request_when_updating_by_existing_sap() throws Exception {
        //given
        String sap = "123456";
        CarrierNewUpdateDTO updateDTO = new CarrierNewUpdateDTO("654321", "Lol Trans", "Testland", null);
        Mockito.when(carrierRestService.updateCarrier(sap, updateDTO)).thenThrow(
                new IllegalOperationException("Carrier with provided SAP already exists, SAP has to be unique"));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/carriers/" + sap).contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                   "sap": "654321",
                                  "name": "Lol Trans",
                                  "origin": "Testland"
                                }
                                """
                ));
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("Carrier with provided SAP already exists, SAP has to be unique")));
    }

    @ParameterizedTest
    @ArgumentsSource(ControllerUpdateCarrierArgumentsProvider.class)
    void updateCarrier_should_return_bad_request_when_updating_not_valid_entry(String newTruckJson, int code,
                                                                               String msg,
                                                                               String[] detailsSap, String[] detailsName,
                                                                               String[] detailsOrigin, String[] detailsRate) throws Exception {

        String sap = "990990";
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/carriers/" + sap)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTruckJson));
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(code)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(msg)));
        if (detailsSap != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.sap").value(containsInAnyOrder(detailsSap)));
        if (detailsName != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.name").value(containsInAnyOrder(detailsName)));
        if (detailsOrigin != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.origin").value(containsInAnyOrder(detailsOrigin)));
        if (detailsRate != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.rate").value(containsInAnyOrder(detailsRate)));
        Mockito.verify(carrierRestService, Mockito.never()).addNewCarrier(Mockito.any());
    }
}