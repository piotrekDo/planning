package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TautlinerController.class)
class TautlinerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TautlinerRestService tautlinerRestService;

    @Test
    void getTautlinerByPlates_should_return_tautliner_entity_if_plates_are_correct() throws Exception {
        //given
        String plates = "ABC1234";
        TautlinerInfoDTO tautliner = new TautlinerInfoDTO(plates, LocalDateTime.of(2022, 11, 11, 0, 0, 0), true, null, null, null);
        Mockito.when(tautlinerRestService.getTautlinerByPlates(plates)).thenReturn(tautliner);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get(("/tautliners/" + plates)).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(tautlinerRestService).getTautlinerByPlates(plates);
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.xpo", equalTo(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautlinerPlates", equalTo(plates)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.techInspection", equalTo("2022-11-11T00:00:00")));
    }

    @Test
    void getTatulinerByPlates_should_return_not_found_if_no_such_plates_found() throws Exception {
        //given
        String plates = "ABC1234";
        Mockito.when(tautlinerRestService.getTautlinerByPlates(plates)).thenThrow(
                new NoSuchElementException("No tautliner found with plates: " + plates));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/tautliners/" + plates).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(tautlinerRestService).getTautlinerByPlates(plates);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No tautliner found with plates: " + plates)));
    }

    @Test
    void addNewTautliner_to_carier_should_provide_to_service_and_return_code_ok() throws Exception {
        //given
        Long carrierId = 43L;
        TautlinerNewUpdateDTO tautliner = new TautlinerNewUpdateDTO(false, "ABC1234", "2022-11-10");
        TautlinerEntity tautlinerEntity = new TautlinerEntity(12L, tautliner.getXpo(), tautliner.getTautlinerPlates(), LocalDateTime.of(2022, 11, 10, 0, 0, 0), null, null);
        CarrierEntity carrier = new CarrierEntity(carrierId, "123456", "Test Carrier", "TestLand", 1.2, null, null, List.of(tautlinerEntity));
        TautlinerInfoDTO info = new TautlinerInfoDTO(tautlinerEntity.getTautlinerPlates(), tautlinerEntity.getTechInspection(), tautlinerEntity.getXpo(), carrier.getName(), carrier.getSap(), null);
        Mockito.when(tautlinerRestService.addNewTautliner(carrierId, tautliner)).thenReturn(info);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/tautliners/" + carrierId).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "xpo": false,
                            "tautlinerPlates": "ABC1234",
                            "techInspection": "2022-11-10"
                        }
                                """));

        //then
        Mockito.verify(tautlinerRestService).addNewTautliner(carrierId, tautliner);
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.xpo", equalTo(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautlinerPlates", equalTo("ABC1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.techInspection", equalTo("2022-11-10T00:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carrierName", equalTo("Test Carrier")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carrierSap", equalTo("123456")));
    }

    @Test
    void addNewTautliner_should_return_bad_request_when_adding_to_non_existing_carrier() throws Exception {
        //given
        Long carrierId = 123L;
        TautlinerNewUpdateDTO tautliner = new TautlinerNewUpdateDTO(false, "ABC1234", "2022-11-10");
        Mockito.when(tautlinerRestService.addNewTautliner(carrierId, tautliner)).thenThrow(
                new NoSuchElementException("No carrier found with id: " + carrierId));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/tautliners/" + carrierId).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "xpo": false,
                            "tautlinerPlates": "ABC1234",
                            "techInspection": "2022-11-10"
                        }
                                """));

        //then
        Mockito.verify(tautlinerRestService).addNewTautliner(carrierId, tautliner);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier found with id: " + carrierId)));
    }

    @Test
    void addNewTautliner_should_return_bad_request_when_adding_tautliner_with_existing_plates() throws Exception {
        //given
        Long carrierId = 123L;
        TautlinerNewUpdateDTO tautliner = new TautlinerNewUpdateDTO(false, "ABC1234", "2022-11-10");
        Mockito.when(tautlinerRestService.addNewTautliner(carrierId, tautliner)).thenThrow(
                new IllegalOperationException(String.format("Tautliner with plates %s already exists!", tautliner.getTautlinerPlates()))
        );

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/tautliners/" + carrierId).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "xpo": false,
                            "tautlinerPlates": "ABC1234",
                            "techInspection": "2022-11-10"
                        }
                                """));

        //then
        Mockito.verify(tautlinerRestService).addNewTautliner(carrierId, tautliner);
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo(String.format("Tautliner with plates %s already exists!", tautliner.getTautlinerPlates()))));
    }

    @Test
    void deleteTautliner_should_return_code_200_when_deleting_by_existing_plates() throws Exception {
        //given
        String tautlinerPlates = "ABCD1234";
        TautlinerEntity tautlinerEntity = new TautlinerEntity(true, tautlinerPlates, null, null, null);
        Mockito.when(tautlinerRestService.deleteTautlinerByPlates(tautlinerPlates)).thenReturn(tautlinerEntity);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/tautliners/" + tautlinerPlates).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautlinerPlates", equalTo(tautlinerPlates)));
        Mockito.verify(tautlinerRestService).deleteTautlinerByPlates(tautlinerPlates);
    }

    @Test
    void deleteTautliner_should_return_not_found_if_deleting_by_non_existing_plates() throws Exception {
        //given
        String tautlinerPlates = "ABCD1234";
        Mockito.when(tautlinerRestService.deleteTautlinerByPlates(tautlinerPlates)).thenThrow(
                new NoSuchElementException("No tautliner found with plates: " + tautlinerPlates)
        );

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/tautliners/" + tautlinerPlates).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No tautliner found with plates: " + tautlinerPlates)));
    }

}