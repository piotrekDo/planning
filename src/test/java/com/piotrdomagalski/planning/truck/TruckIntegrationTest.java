package com.piotrdomagalski.planning.truck;


import com.piotrdomagalski.planning.PlanningApplication;
import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;
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
class TruckIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TruckRepository truckRepository;

    @Autowired
    CarrierRepository carrierRepository;

    @Test
    void requesting_all_trucks_where_no_truck_was_added_should_return_code_200_and_empty_list() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/trucks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", equalTo(Collections.emptyList())));
    }

    @Test
    void getTruckByPlates_should_return_code_200_and_response_body_if_plates_was_found() throws Exception {
        //given
        TruckEntity truck = TruckEntity.newTruck("TRUCK1234", true);
        truckRepository.save(truck);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/trucks/" + "TRUCK1234").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.truckPlates", equalTo("TRUCK1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mega", equalTo(true)));
    }

    @Test
    void getTruckByPlates_should_return_not_found_if_plates_were_not_found() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/trucks/" + "TRUCK1234").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No truck found with plates: TRUCK1234")));
    }

    @Test
    void adding_truck_should_return_code_200_and_response_body() throws Exception {
        //given
        CarrierEntity carrierEntity = CarrierEntity.newCarrier("123456", "Test Carrier", "Testland", 1.2);
        carrierRepository.save(carrierEntity);

        //when
        //when+then
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/trucks/" + "123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                              "mega": true,
                              "truckPlates": "TRUCK1234"
                        }
                        """));
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.truckPlates", equalTo("TRUCK1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carrierName", equalTo("Test Carrier")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carrierSap", equalTo("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mega", equalTo(true)));

        Optional<TruckEntity> truckByiD = truckRepository.findById(1L);
        assertNotNull(truckByiD);
    }

    @Test
    void adding_truck_to_non_existing_carrier_should_return_not_found() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/trucks/" + "123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                              "mega": true,
                              "truckPlates": "TRUCK1234"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier with sap: 123456")));
    }

    @Test
    void deleteTruckByPlates_should_return_code_200_and_delete_entity() throws Exception {
        //given
        TruckEntity truck = TruckEntity.newTruck("TRUCK1234", true);
        truckRepository.save(truck);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/trucks/" + "TRUCK1234").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.truckPlates", equalTo("TRUCK1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mega", equalTo(true)));
        Optional<TruckEntity> truckResult = truckRepository.findByTruckPlatesIgnoreCase("TRUCK1234");
        assertEquals(Optional.empty(), truckResult);
    }

    @Test
    void deleteTruckByPlates_should_return_not_found_when_deleting_by_non_existing_plates() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/trucks/" + "TRUCK1234").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No truck found with plates: TRUCK1234")));
    }

    @ParameterizedTest
    @ArgumentsSource(TruckIntegrationTestUpdateArugmentsProvider.class)
    void updateTruck_should_reutn_conde_200_and_response_body_if_updated(TruckNewUpdateDTO updateDto, String updateJson,
                                                                         TruckNewUpdateDTO result, TruckEntity resultEntity) throws Exception {
        //given
        TruckEntity truck = TruckEntity.newTruck("TRUCK1234", true);
        truckRepository.save(truck);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/trucks/" + "TRUCK1234").contentType(MediaType.APPLICATION_JSON)
                .content(updateJson));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.truckPlates", equalTo(result.getTruckPlates())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mega", equalTo(result.getMega())));
        assertEquals(resultEntity, truckRepository.findByTruckPlatesIgnoreCase(result.getTruckPlates()).get());
    }


    @Test
    void updateTruck_should_return_not_found_when_no_provided_plates_exists() throws Exception {
        //given
        String plates = "TRUCK1234";
        TruckNewUpdateDTO updateDTO = new TruckNewUpdateDTO("TEST1234", null);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/trucks/" + plates).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "truckPlates": "TEST1234"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No truck found with plates: " + plates)));
    }

    @Test
    void updateTruck_should_return_bad_reqiest_when_plates_provided_to_change_already_exist() throws Exception {
        //given
        TruckEntity truck = TruckEntity.newTruck("TRUCK1234", true);
        TruckEntity truck2 = TruckEntity.newTruck("TEST1234", false);
        truckRepository.save(truck);
        truckRepository.save(truck2);
        String plates = "TRUCK1234";
        TruckNewUpdateDTO updateDTO = new TruckNewUpdateDTO("TEST1234", null);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/trucks/" + plates).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "truckPlates": "TEST1234"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("Truck with provided plates already exists!, plates has to be unique")));
    }
}
