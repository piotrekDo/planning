package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.app_user.AppUserService;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.error.IllegalOperationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TautlinerController.class)
@WithMockUser(username = "Test", authorities = {"USER"})
class TautlinerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AppUserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    TautlinerService tautlinerService;

    @Test
    void getTautlinerByPlates_should_return_tautliner_entity_if_plates_are_correct() throws Exception {
        //given
        String plates = "ABC1234";
        TautlinerInfoDTO tautliner = new TautlinerInfoDTO(plates, LocalDateTime.of(2022, 11, 11, 0, 0, 0), true, null, null, null);
        Mockito.when(tautlinerService.getTautlinerByPlates(plates)).thenReturn(tautliner);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get(("/tautliners/" + plates)).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(tautlinerService).getTautlinerByPlates(plates);
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.xpo", equalTo(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautlinerPlates", equalTo(plates)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.techInspection", equalTo("2022-11-11T00:00:00")));
    }

    @Test
    void getTatulinerByPlates_should_return_not_found_if_no_such_plates_found() throws Exception {
        //given
        String plates = "ABC1234";
        Mockito.when(tautlinerService.getTautlinerByPlates(plates)).thenThrow(
                new NoSuchElementException("No tautliner found with plates: " + plates));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/tautliners/" + plates).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(tautlinerService).getTautlinerByPlates(plates);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No tautliner found with plates: " + plates)));
    }

    @Test
    void addNewTautliner_to_carier_should_provide_to_service_and_return_code_ok() throws Exception {
        //given
        String carrierSap = "123456";
        TautlinerNewUpdateDTO tautliner = new TautlinerNewUpdateDTO(false, "ABC1234", "2022-11-10");
        TautlinerEntity tautlinerEntity = new TautlinerEntity(12L, tautliner.getXpo(), tautliner.getTautlinerPlates(), LocalDateTime.of(2022, 11, 10, 0, 0, 0), null, null);
        CarrierEntity carrier = new CarrierEntity(99L, carrierSap, "Test Carrier", "TestLand", 1.2, null, null, List.of(tautlinerEntity));
        TautlinerInfoDTO info = new TautlinerInfoDTO(tautlinerEntity.getTautlinerPlates(), tautlinerEntity.getTechInspection(), tautlinerEntity.getXpo(), carrier.getName(), carrier.getSap(), null);
        Mockito.when(tautlinerService.addNewTautliner(carrierSap, tautliner)).thenReturn(info);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/tautliners/" + carrierSap).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "xpo": false,
                            "tautlinerPlates": "ABC1234",
                            "techInspection": "2022-11-10"
                        }
                                """));

        //then
        Mockito.verify(tautlinerService).addNewTautliner(carrierSap, tautliner);
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
        String carrierSap = "123456";
        TautlinerNewUpdateDTO tautliner = new TautlinerNewUpdateDTO(false, "ABC1234", "2022-11-10");
        Mockito.when(tautlinerService.addNewTautliner(carrierSap, tautliner)).thenThrow(
                new NoSuchElementException("No carrier found with sap: " + carrierSap));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/tautliners/" + carrierSap).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "xpo": false,
                            "tautlinerPlates": "ABC1234",
                            "techInspection": "2022-11-10"
                        }
                                """));

        //then
        Mockito.verify(tautlinerService).addNewTautliner(carrierSap, tautliner);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier found with sap: " + carrierSap)));
    }

    @Test
    void addNewTautliner_should_return_bad_request_when_adding_tautliner_with_existing_plates() throws Exception {
        //given
        String carrierSap = "123456";
        TautlinerNewUpdateDTO tautliner = new TautlinerNewUpdateDTO(false, "ABC1234", "2022-11-10");
        Mockito.when(tautlinerService.addNewTautliner(carrierSap, tautliner)).thenThrow(
                new IllegalOperationException(String.format("Tautliner with plates %s already exists!", tautliner.getTautlinerPlates()))
        );

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/tautliners/" + carrierSap).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "xpo": false,
                            "tautlinerPlates": "ABC1234",
                            "techInspection": "2022-11-10"
                        }
                                """));

        //then
        Mockito.verify(tautlinerService).addNewTautliner(carrierSap, tautliner);
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo(String.format("Tautliner with plates %s already exists!", tautliner.getTautlinerPlates()))));
    }

    @Test
    void deleteTautliner_should_return_code_403_if_attempted_by_not_allowed_user() throws Exception {
        //given
        String tautlinerPlates = "ABCD1234";

        //when + then
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/tautliners/" + tautlinerPlates).contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(MockMvcResultMatchers.status().isForbidden());
        Mockito.verify(tautlinerService, Mockito.never()).deleteTautlinerByPlates(Mockito.any());
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"MODERATOR"})
    void deleteTautliner_should_return_code_200_when_deleting_by_existing_plates() throws Exception {
        //given
        String tautlinerPlates = "ABCD1234";
        TautlinerEntity tautlinerEntity = new TautlinerEntity(true, tautlinerPlates, null, null, null);
        Mockito.when(tautlinerService.deleteTautlinerByPlates(tautlinerPlates)).thenReturn(tautlinerEntity);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/tautliners/" + tautlinerPlates).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautlinerPlates", equalTo(tautlinerPlates)));
        Mockito.verify(tautlinerService).deleteTautlinerByPlates(tautlinerPlates);
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"MODERATOR"})
    void deleteTautliner_should_return_not_found_if_deleting_by_non_existing_plates() throws Exception {
        //given
        String tautlinerPlates = "ABCD1234";
        Mockito.when(tautlinerService.deleteTautlinerByPlates(tautlinerPlates)).thenThrow(
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

    @ParameterizedTest
    @ArgumentsSource(TautlinerControllerUpdateArugmentsProvider.class)
    void updateTruck_should_reutn_conde_200_and_response_body_if_updated(TautlinerNewUpdateDTO updateDto, String updateJson, TautlinerNewUpdateDTO result) throws Exception {
        //given
        String plates = "TAUT1234";
        Mockito.when(tautlinerService.updateTautlinerByPlates(plates, updateDto)).thenReturn(result);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/tautliners/" + plates).contentType(MediaType.APPLICATION_JSON)
                .content(updateJson));

        //then
        Mockito.verify(tautlinerService).updateTautlinerByPlates(plates, updateDto);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautlinerPlates", equalTo(result.getTautlinerPlates())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.techInspection", equalTo(result.getTechInspection())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.xpo", equalTo(result.getXpo())));
    }

    @Test
    void updateTautliner_should_return_not_found_when_provided_plates_doesnt_exists() throws Exception {
        //given
        String plates = "TAUT1234";
        TautlinerNewUpdateDTO tautliner = new TautlinerNewUpdateDTO(true, "TAUT1234", null);
        Mockito.when(tautlinerService.updateTautlinerByPlates(plates, tautliner)).thenThrow(
                new NoSuchElementException("No tautliner found with plates: " + plates)
        );

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/tautliners/" + plates).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "tautlinerPlates": "TAUT1234",
                          "xpo": true
                        }
                        """));

        //then
        Mockito.verify(tautlinerService).updateTautlinerByPlates(plates, tautliner);
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No tautliner found with plates: " + plates)));
    }

    @Test
    void updateTautliner_should_return_bad_reqiest_when_plates_provided_to_change_already_exist() throws Exception {
        //given
        String plates = "TAUT1234";
        TautlinerNewUpdateDTO tautliner = new TautlinerNewUpdateDTO(null, "NEW12345", null);
        Mockito.when(tautlinerService.updateTautlinerByPlates(plates, tautliner)).thenThrow(
                new IllegalOperationException(String.format("Tautliner with plates: %s already exists", tautliner.getTautlinerPlates())));


        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/tautliners/" + plates).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "tautlinerPlates": "NEW12345"
                        }
                        """));

        //then
        Mockito.verify(tautlinerService).updateTautlinerByPlates(plates, tautliner);
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo(String.format("Tautliner with plates: %s already exists", tautliner.getTautlinerPlates()))));
    }

    @ParameterizedTest
    @ArgumentsSource(TautlinerControllerUpdateNotValidArgumentsProvider.class)
    void updateTruck_should_return_bad_request_when_updating_with_not_valid_data(String json, int code, String name, String[] details) throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/tautliners/" + "TAUT1234").contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        Mockito.verify(tautlinerService, Mockito.never()).updateTautlinerByPlates(Mockito.any(), Mockito.any());
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(code)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(name)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details.tautlinerPlates").value(containsInAnyOrder(details)));

    }

}