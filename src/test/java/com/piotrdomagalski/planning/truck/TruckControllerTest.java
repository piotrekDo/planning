package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.app.IllegalOperationException;
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

import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TruckController.class)
class TruckControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TruckRestService truckRestService;

    @Test
    void getTruckByPlates_should_return_code_200_and_response_body_if_such_plates_exists() throws Exception {
        //given
        String plates = "ABC1234";
        TruckInfoDTO truck = new TruckInfoDTO(plates, true, "123456", "Test carrier", null, null, null, null, null, null, null);

        Mockito.when(truckRestService.getTruckByPlates(plates)).thenReturn(truck);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get(("/trucks/" + plates)).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(truckRestService).getTruckByPlates(plates);
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.truckPlates", equalTo("ABC1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mega", equalTo(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carrierSap", equalTo("123456")));
    }

    @Test
    void getTruckByPlates_should_return_not_found_if_no_such_plates_found() throws Exception {
        //given
        String plates = "ABC1234";
        Mockito.when(truckRestService.getTruckByPlates(plates)).thenThrow(
                new NoSuchElementException("No truck found with plates: " + plates));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/trucks/" + plates).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(truckRestService).getTruckByPlates(plates);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No truck found with plates: " + plates)));
    }

    @Test
    void addNewTruck_should_provide_to_service_and_return_code_ok() throws Exception {
        //given
        String carrierSap = "123456";
        TruckNewUpdateDTO newUpdateDTO = new TruckNewUpdateDTO("avb5690", true);
        TruckInfoDTO truck = new TruckInfoDTO("AVB5690", true, "123456", "Test carrier", null, null, null, null, null, null, null);
        Mockito.when(truckRestService.addNewTruck(carrierSap, newUpdateDTO)).thenReturn(truck);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/trucks/" + carrierSap).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "mega": true,
                             "truckPlates": "avb5690"
                        }
                                """));

        //then
        Mockito.verify(truckRestService).addNewTruck(carrierSap, newUpdateDTO);
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.truckPlates", equalTo("AVB5690")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mega", equalTo(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carrierName", equalTo("Test carrier")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carrierSap", equalTo("123456")));
    }

    @Test
    void addNewTruck_should_return_bad_request_when_adding_to_non_existing_carrier() throws Exception {
        //given
        String carrierSap = "123456";
        TruckNewUpdateDTO newUpdateDTO = new TruckNewUpdateDTO("avb5690", true);
        Mockito.when(truckRestService.addNewTruck(carrierSap, newUpdateDTO)).thenThrow(
                new NoSuchElementException("No carrier found with sap: " + carrierSap));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/trucks/" + carrierSap).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "mega": true,
                             "truckPlates": "avb5690"
                        }
                                """));

        //then
        Mockito.verify(truckRestService).addNewTruck(carrierSap, newUpdateDTO);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier found with sap: " + carrierSap)));
    }

    @Test
    void addNewTruck_should_return_bad_request_when_adding_truck_with_existing_plates() throws Exception {
        //given
        String carrierSap = "123456";
        TruckNewUpdateDTO newUpdateDTO = new TruckNewUpdateDTO("avb5690", true);
        Mockito.when(truckRestService.addNewTruck(carrierSap, newUpdateDTO)).thenThrow(
                new IllegalOperationException(String.format("Truck with plates %s already exists!", newUpdateDTO.getTruckPlates()))
        );

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/trucks/" + carrierSap).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "mega": true,
                             "truckPlates": "avb5690"
                        }
                                """));

        //then
        Mockito.verify(truckRestService).addNewTruck(carrierSap, newUpdateDTO);
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo(String.format("Truck with plates %s already exists!", newUpdateDTO.getTruckPlates()))));
    }

    @ParameterizedTest
    @ArgumentsSource(ControllerAddNewTruckArgumentsProvider.class)
    void addNewTruck_should_return_bad_request_when_adding_not_valid_entry(String newTruckJson, int code,
                                                                           String msg, String[] detailsTruckPlates, String[] detailsIsMega) throws Exception {
        long carrierId = 1L;
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/trucks/" + carrierId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTruckJson));
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(code)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(msg)));
        if (detailsTruckPlates != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.truckPlates").value(containsInAnyOrder(detailsTruckPlates)));
        if (detailsIsMega != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.mega").value(containsInAnyOrder(detailsIsMega)));
        Mockito.verify(truckRestService, Mockito.never()).addNewTruck(Mockito.any(), Mockito.any());
    }

    @Test
    void deleteTruck_should_return_code_200_when_deleting_by_existing_plates() throws Exception {
        //given
        String truckPlates = "ABCD1234";
        TruckEntity truckEntity = new TruckEntity(12L, truckPlates, false, null, null, null);
        Mockito.when(truckRestService.deleteTruckByPlates(truckPlates)).thenReturn(truckEntity);


        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/trucks/" + truckPlates).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(truckRestService).deleteTruckByPlates(truckPlates);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.truckPlates", equalTo(truckPlates)));
    }

    @Test
    void deleteTruck_should_return_not_found_if_deleting_by_non_existing_plates() throws Exception {
        //given
        String truckPlates = "ABCD1234";
        Mockito.when(truckRestService.deleteTruckByPlates(truckPlates)).thenThrow(
                new NoSuchElementException("No truck found with plates: " + truckPlates)
        );

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/trucks/" + truckPlates).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No truck found with plates: " + truckPlates)));
    }

    @ParameterizedTest
    @ArgumentsSource(TruckControllerUpdateArugmentsProvider.class)
    void updateTruck_should_reutn_conde_200_and_response_body_if_updated(String plates, TruckNewUpdateDTO updateDto, String updateJson, TruckNewUpdateDTO result) throws Exception {
        //given
        Mockito.when(truckRestService.updateTruck(plates, updateDto)).thenReturn(result);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/trucks/" + plates).contentType(MediaType.APPLICATION_JSON)
                .content(updateJson));

        //then
        Mockito.verify(truckRestService).updateTruck(plates, updateDto);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.truckPlates", equalTo(result.getTruckPlates())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mega", equalTo(result.getMega())));
    }

    @Test
    void updateTruck_should_return_not_found_when_provided_plates_doesnt_exists() throws Exception {
        //given
        String plates = "TRUCK1234";
        TruckNewUpdateDTO updateDTO = new TruckNewUpdateDTO("TEST1234", null);
        Mockito.when(truckRestService.updateTruck(plates, updateDTO)).thenThrow(
                new NoSuchElementException("No truck found with plates: " + plates)
        );

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/trucks/" + plates).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "truckPlates": "TEST1234"
                        }
                        """));

        //then
        Mockito.verify(truckRestService).updateTruck(plates, updateDTO);
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No truck found with plates: " + plates)));
    }

    @Test
    void updateTruck_should_return_bad_reqiest_when_plates_provided_to_change_already_exist() throws Exception {
        //given
        String plates = "TRUCK1234";
        TruckNewUpdateDTO updateDTO = new TruckNewUpdateDTO("TEST1234", null);
        Mockito.when(truckRestService.updateTruck(plates, updateDTO)).thenThrow(
                new IllegalOperationException("Truck with provided plates already exists!, plates has to be unique")
        );

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/trucks/" + plates).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "truckPlates": "TEST1234"
                        }
                        """));

        //then
        Mockito.verify(truckRestService).updateTruck(plates, updateDTO);
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("Truck with provided plates already exists!, plates has to be unique")));
    }

    @ParameterizedTest
    @ArgumentsSource(TruckControllerUpdateNotValidArgumentsProvider.class)
    void updateTruck_should_return_bad_request_when_updating_with_not_valid_data(String json, int code, String name, String[] details) throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/trucks/" + "ABC1234").contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        Mockito.verify(truckRestService, Mockito.never()).updateTruck(Mockito.any(), Mockito.any());
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(code)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(name)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details.truckPlates").value(containsInAnyOrder(details)));

    }

}