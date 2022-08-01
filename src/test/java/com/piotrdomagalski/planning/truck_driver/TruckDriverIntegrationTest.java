package com.piotrdomagalski.planning.truck_driver;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PlanningApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TruckDriverIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TruckDriverRepository truckDriverRepository;

    @Autowired
    CarrierRepository carrierRepository;

    @Test
    void requesting_all_drivers_where_no_driver_was_added_should_return_code_200_and_empty_list() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/drivers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", equalTo(Collections.emptyList())));
    }

    @Test
    void adding_truck_driver_should_return_code_200_and_response_body() throws Exception {
        //given
        CarrierEntity carrierEntity = CarrierEntity.newCarrier("123456", "Test Carrier", "Testland", 1.2);
        carrierRepository.save(carrierEntity);

        //when
        //when+then
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/drivers/" + "123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "fullName": "Test one",
                            "idDocument": "ID123456",
                            "tel": "999000888"
                        }
                        """));
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", equalTo("Test One")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tel", equalTo("999-000-888")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocument", equalTo("ID123456")));

        Optional<TruckDriverEntity> truckDriverById = truckDriverRepository.findById(1L);
        assertNotNull(truckDriverById);
    }

    @Test
    void adding_truck_driver_to_non_existing_carrier_should_return_not_found() throws Exception {
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/drivers/" + "123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "fullName": "Test one",
                            "idDocument": "ID123456",
                            "tel": "999000888"
                        }
                        """));
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier with sap: 123456")));
    }

    @Test
    void deleteDriverById_should_return_code_200_and_delete_entity() throws Exception {
        //given
        TruckDriverEntity truckDriverEntity = TruckDriverEntity.newTruckDriver("Test Driver", "444-444-444", "ID123456");
        truckDriverRepository.save(truckDriverEntity);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/drivers/" + 1).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", equalTo("Test Driver")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tel", equalTo("444-444-444")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocument", equalTo("ID123456")));
        Optional<TruckDriverEntity> driver = truckDriverRepository.findById(1L);
        assertEquals(Optional.empty(), driver);
    }

    @Test
    void deleteDriverById_should_return_not_found_when_deleting_by_non_existing_id() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/drivers/" + 1).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No driver found with id: 1")));
    }

    @ParameterizedTest
    @ArgumentsSource(TruckDriverIntegrationTestUpdateArugmentsProvider.class)
    void updateDriver_should_return_code_200_and_updated_truck_driver(String json, TruckDriverNewUpdateDTO resultDto,
                                                                      TruckDriverEntity resultEntity) throws Exception {
        //given
        TruckDriverEntity truckDriverEntity = TruckDriverEntity.newTruckDriver("Test Driver", "111-111-111", "ID123456");
        truckDriverRepository.save(truckDriverEntity);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/drivers/" + 1).contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.fullName", equalTo(resultDto.getFullName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tel", equalTo(resultDto.getTel())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocument", equalTo(resultDto.getIdDocument())));
        TruckDriverEntity entity = truckDriverRepository.findById(1L).get();
        assertEquals(resultEntity, entity);
        assertEquals(resultEntity.getFullName(), entity.getFullName());
        assertEquals(resultEntity.getTel(), entity.getTel());
        assertEquals(resultEntity.getIdDocument(), entity.getIdDocument());
    }

    @Test
    void updateDriver_should_return_not_found_when_updating_by_non_existing_id() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/drivers/" + 1).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "fullName": "New Name"
                        }
                                """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No driver found with id: 1")));
    }
}
