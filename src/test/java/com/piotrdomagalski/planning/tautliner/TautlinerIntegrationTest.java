package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.PlanningApplication;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PlanningApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WithMockUser(username = "Test", authorities = {"USER"})
class TautlinerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TautlinerRepository tautlinerRepository;

    @Autowired
    CarrierRepository carrierRepository;


    @Test
    void requesting_all_tautliners_where_no_tautliner_was_added_should_return_code_200_and_empty_list() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tautliners").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", equalTo(Collections.emptyList())));
    }

    @Test
    void getTautlinerByPlates_should_return_code_200_and_response_body_if_plates_were_found() throws Exception {
        //given
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, "TAUT1234", LocalDateTime.of(2022, 10, 10, 0, 0, 0));
        tautlinerRepository.save(tautliner);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/tautliners/" + "TAUT1234").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautlinerPlates", equalTo("TAUT1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.xpo", equalTo(true)));
    }

    @Test
    void getTautlinerByPlates_should_return_not_found_if_plates_were_not_found() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/tautliners/" + "TAUT1234").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No tautliner found with plates: TAUT1234")));
    }

    @Test
    void adding_tautliner_should_return_code_200_and_response_body() throws Exception {
        //given
        CarrierEntity carrierEntity = CarrierEntity.newCarrier("123456", "Test Carrier", "Testland", 1.2);
        carrierRepository.save(carrierEntity);

        //when
        //when+then
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/tautliners/" + "123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                                "tautlinerPlates": "TAUT123",
                                "techInspection": "2022-10-10",
                                "xpo": true
                        }
                        """));
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautlinerPlates", equalTo("TAUT123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.xpo", equalTo(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.techInspection", equalTo("2022-10-10T00:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carrierSap", equalTo("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.xpo", equalTo(true)));

        Optional<TautlinerEntity> tautlinerById = tautlinerRepository.findById(1L);
        assertNotNull(tautlinerById);
    }

    @Test
    void adding_truck_to_non_existing_carrier_should_return_not_found() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/tautliners/" + "123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                                "tautlinerPlates": "TAUT123",
                                "techInspection": "2022-10-10",
                                "xpo": true
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier found with sap: 123456")));
    }

    @Test
    void deleteTautlinerByPlates_should_return_code_403_if_attempted_by_not_allowed_user() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/tautliners/" + "TAUT1234").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"MODERATOR"})
    void deleteTautlinerByPlates_should_return_code_200_and_delete_entity() throws Exception {
        //given
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, "TAUT1234", LocalDateTime.of(2022, 10, 10, 0, 0, 0));
        tautlinerRepository.save(tautliner);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/tautliners/" + "TAUT1234").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautlinerPlates", equalTo("TAUT1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.xpo", equalTo(true)));
        Optional<TautlinerEntity> tautResult = tautlinerRepository.findByTautlinerPlatesIgnoreCase("TAUT1234");
        assertEquals(Optional.empty(), tautResult);
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"MODERATOR"})
    void deleteTautlinerByPlates_should_return_not_found_when_deleting_by_non_existing_plates() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/tautliners/" + "TAUT1234").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No tautliner found with plates: TAUT1234")));
    }

    @ParameterizedTest
    @ArgumentsSource(TautlinerIntegrationTestUpdateArugmentsProvider.class)
    void updateTautliner_should_reutn_conde_200_and_response_body_if_updated(String updateJson, TautlinerNewUpdateDTO result, TautlinerEntity resultEntity) throws Exception {
        //given
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, "TAUT1234", LocalDateTime.of(2023, 10, 10, 0, 0, 0));
        tautlinerRepository.save(tautliner);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/tautliners/" + "TAUT1234").contentType(MediaType.APPLICATION_JSON)
                .content(updateJson));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tautlinerPlates", equalTo(result.getTautlinerPlates())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.techInspection", equalTo(result.getTechInspection())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.xpo", equalTo(result.getXpo())));
        assertEquals(resultEntity, tautlinerRepository.findByTautlinerPlatesIgnoreCase(result.getTautlinerPlates()).get());
    }

    @Test
    void updateTautliner_should_return_not_found_when_no_provided_plates_exists() throws Exception {
        //given
        String plates = "TAUT1234";

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/tautliners/" + plates).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "tautlinerPlates": "TEST1234"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No tautliner found with plates: " + plates)));
    }

    @Test
    void updateTautliner_should_return_bad_reqiest_when_plates_provided_to_change_already_exist() throws Exception {
        //given
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, "TAUT1234", LocalDateTime.of(2023, 10, 10, 0, 0, 0));
        TautlinerEntity tautliner2 = TautlinerEntity.newTautliner(false, "TEST1234", LocalDateTime.of(2023, 05, 10, 0, 0, 0));
        tautlinerRepository.save(tautliner);
        tautlinerRepository.save(tautliner2);
        String plates = "TAUT1234";

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/tautliners/" + plates).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "tautlinerPlates": "TEST1234"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo(String.format("Tautliner with plates: %s already exists", tautliner2.getTautlinerPlates()))));
    }

}
