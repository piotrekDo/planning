package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.PlanningApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
public class CarrierIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CarrierRepository carrierRepository;

    @Test
    void requesting_all_carriers_where_no_carrier_was_added_should_return_code_200_and_empty_list() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/carriers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", equalTo(Collections.emptyList())));
    }

    @Test
    void requesting_all_carriers_short_where_no_carrier_was_added_should_return_code_200_and_empty_list() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/carriers/all-short").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", equalTo(Collections.emptyList())));
    }

    @Test
    void getCarrierBySap_should_return_code_200_and_response_body_if_sap_was_found() throws Exception {
        //given
        CarrierEntity carrierEntity = CarrierEntity.newCarrier("123456", "Test Carrier", "Testland", 1.2);
        carrierRepository.save(carrierEntity);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/carriers/" + "123456").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sap", equalTo("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Test Carrier")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.origin", equalTo("Testland")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate", equalTo(1.2)));
    }

    @Test
    void getCarrierBySap_should_return_not_found_if_rate_were_not_found() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/carriers/" + "123456").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier found with id: 123456")));
    }

    @Test
    void adding_carrier_should_return_code_200_and_response_body() throws Exception {
        //when
        //when+then
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/carriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                                  "name": "Test Carrier",
                                  "origin": "Testland",
                                  "rate": 1.2,
                                  "sap": "123456"
                        }
                        """));
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sap", equalTo("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Test Carrier")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.origin", equalTo("Testland")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate", equalTo(1.2)));

        Optional<CarrierEntity> carrierBySap = carrierRepository.findBySap("123456");
        assertNotNull(carrierBySap);
    }

    @Test
    void deleteCarrierBySap_should_return_code_200_and_delete_entity() throws Exception {
        //given
        //given
        CarrierEntity carrierEntity = CarrierEntity.newCarrier("123456", "Test Carrier", "Testland", 1.2);
        carrierRepository.save(carrierEntity);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/carriers/" + "123456").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sap", equalTo("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Test Carrier")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.origin", equalTo("Testland")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate", equalTo(1.2)));
        Optional<CarrierEntity> carrierBySap = carrierRepository.findBySap("123456");
        assertEquals(Optional.empty(), carrierBySap);
    }

    @Test
    void deleteCarrierBySap_should_return_not_found_when_deleting_by_non_existing_sap() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/carriers/" + "123456").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier found with id: 123456")));
    }

    @ParameterizedTest
    @ArgumentsSource(CarrierIntegrationTestUpdateArugmentsProvider.class)
    void updateCarrier_should_reutn_conde_200_and_response_body_if_updated(String updateJson, CarrierNewUpdateDTO result, CarrierEntity resultEntity) throws Exception {
        //given
        CarrierEntity baseCarrier = CarrierEntity.newCarrier("123456", "Test Carrier", "Testland", 1.2);
        carrierRepository.save(baseCarrier);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/carriers/" + "123456").contentType(MediaType.APPLICATION_JSON)
                .content(updateJson));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(result.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.origin", equalTo(result.getOrigin())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate", equalTo(result.getRate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sap", equalTo(result.getSap())));
        assertEquals(resultEntity, carrierRepository.findBySap(result.getSap()).get());
    }

    @Test
    void updateCarrier_should_return_not_found_when_provided_sap_doesnt_exists() throws Exception {
        //given
        String sap = "123456";

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/carriers/" + sap).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "New name",
                          "origin": "New origin"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier found with id: " + sap)));
    }

    @Test
    void updateCarrier_should_return_bad_reqiest_when_sap_provided_to_change_already_exist() throws Exception {
        //given
        CarrierEntity baseCarrier = CarrierEntity.newCarrier("123456", "Test Carrier", "Testland", 1.2);
        CarrierEntity carrier2 = CarrierEntity.newCarrier("987741", "Another Carrier", "Nomanland", 1.1);
        carrierRepository.save(baseCarrier);
        carrierRepository.save(carrier2);
        String sap = "123456";

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/carriers/" + sap).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Yet Another Carrier",
                          "sap": "987741"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("Carrier with provided SAP already exists, SAP has to be unique")));
    }

}
