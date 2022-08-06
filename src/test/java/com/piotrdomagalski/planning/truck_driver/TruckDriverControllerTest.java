package com.piotrdomagalski.planning.truck_driver;

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

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TruckDriverController.class)
class TruckDriverControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TruckDriverRestService driverRestService;

    @Test
    void getDriverById_should_return_driver_entity_if_id_is_correct() throws Exception {
        //given
        long id = 1L;
        TruckDriverInfoDTO dto = new TruckDriverInfoDTO(id, "Andrzej Kowalski", "999-999-999", "ID123456", null, null, null);
        Mockito.when(driverRestService.getTruckDriverById(id)).thenReturn(dto);

        //when+then
        mockMvc.perform(MockMvcRequestBuilders.get("/drivers/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", equalTo("Andrzej Kowalski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tel", equalTo("999-999-999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocument", equalTo("ID123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carrierName", equalTo(null)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carrierSap", equalTo(null)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.truckPlates", equalTo(null)));
        Mockito.verify(driverRestService).getTruckDriverById(id);
    }

    @Test
    void getDriverById_should_throw_an_exception_when_driver_entity_if_id_is_not_correct() throws Exception {
        //given
        Long id = 1L;
        Mockito.when(driverRestService.getTruckDriverById(id)).thenThrow(new NoSuchElementException("No driver found with id: " + id));

        //when+then
        mockMvc.perform(MockMvcRequestBuilders.get("/drivers/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No driver found with id: " + id)));
        Mockito.verify(driverRestService).getTruckDriverById(id);
    }

    @Test
    void addNewDriver_should_be_provided_to_service() throws Exception {
        //given
        Long carrierId = 21L;
        TruckDriverNewUpdateDTO driverDto = new TruckDriverNewUpdateDTO("Test one", "999000888", "ID123456");
        TruckDriverNewUpdateDTO result = new TruckDriverNewUpdateDTO("Test One", "999-000-888", "ID123456");
        Mockito.when(driverRestService.addNewDriver(carrierId, driverDto)).thenReturn(result);

        //when+then
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/drivers/" + carrierId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "fullName": "Test one",
                            "idDocument": "ID123456",
                            "tel": "999000888"
                        }
                        """));
        Mockito.verify(driverRestService).addNewDriver(carrierId, driverDto);
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.fullName", equalTo("Test One")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tel", equalTo("999-000-888")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocument", equalTo("ID123456")));
    }

    @Test
    void addNewDriver_should_return_bad_request_when_adding_to_non_existing_carrier() throws Exception {
        Long carrierId = 21L;
        TruckDriverNewUpdateDTO driverDto = new TruckDriverNewUpdateDTO("Test one", "999000888", "ID123456");
        Mockito.when(driverRestService.addNewDriver(carrierId, driverDto)).thenThrow(new NoSuchElementException("No carrier with id: " + carrierId));

        //when+then
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/drivers/" + carrierId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "fullName": "Test one",
                            "idDocument": "ID123456",
                            "tel": "999000888"
                        }
                                """));
        Mockito.verify(driverRestService).addNewDriver(carrierId, driverDto);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No carrier with id: " + carrierId)));
    }

    @ParameterizedTest
    @ArgumentsSource(ControllerAddNewDriverArgumentsProvider.class)
    void addNewDriver_should_return_bad_request_when_adding_not_valid_entry(String newDriverJson, int code,
                                                                            String msg, String[] detailsFullname, String[] detailsTel, String[] detailsId) throws Exception {
        long carrierId = 1L;
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/drivers/" + carrierId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newDriverJson));
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(code)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(msg)));
        if (detailsFullname != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.fullName").value(containsInAnyOrder(detailsFullname)));
        if (detailsTel != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.tel").value(containsInAnyOrder(detailsTel)));
        if (detailsId != null)
            perform.andExpect(MockMvcResultMatchers.jsonPath("$.details.idDocument").value(containsInAnyOrder(detailsId)));
        Mockito.verify(driverRestService, Mockito.never()).addNewDriver(Mockito.any(), Mockito.any());
    }

    @Test
    void deleteDriverById_should_throw_not_found_error_when_removing_by_non_existing_id() throws Exception {
        //given
        Long id = 1L;
        Mockito.when(driverRestService.deleteTruckDriverById(id)).thenThrow(new NoSuchElementException("No driver found with id: " + id));

        //when+then
        mockMvc.perform(MockMvcRequestBuilders.delete("/drivers/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No driver found with id: " + id)));
        Mockito.verify(driverRestService).deleteTruckDriverById(id);
    }

    @Test
    void deleteDriverById_should_return_removed_driver_when_removal_action_was_successfull() throws Exception {
        //given
        Long id = 1L;
        TruckDriverEntity driver = new TruckDriverEntity(id, "ABC driver", "555444333", "ID123456", null, null);
        Mockito.when(driverRestService.deleteTruckDriverById(id)).thenReturn(driver);

        //when+then
        mockMvc.perform(MockMvcRequestBuilders.delete("/drivers/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", equalTo("ABC driver")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tel", equalTo("555444333")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocument", equalTo("ID123456")));

        Mockito.verify(driverRestService).deleteTruckDriverById(id);
    }

    @Test
    void updateDriver_should_throw_not_found_error_when_trying_to_update_by_non_existing_id() throws Exception {
        //given
        Long id = 321L;
        TruckDriverNewUpdateDTO dto = new TruckDriverNewUpdateDTO("new name", null, null);
        Mockito.when(driverRestService.updateTruckDriver(id, dto)).thenThrow(new NoSuchElementException("No driver found with id: " + id));

        //when+then
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/drivers/" + id).contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "fullName": "new name"
                }
                        """));
        Mockito.verify(driverRestService).updateTruckDriver(id, dto);
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No driver found with id: " + id)));
    }

    @Test
    void updateDriver_should_return_updated_driver_if_update_was_successfull() throws Exception {
        //given
        Long id = 321L;
        TruckDriverNewUpdateDTO dto = new TruckDriverNewUpdateDTO("new name", null, null);
        TruckDriverNewUpdateDTO result = new TruckDriverNewUpdateDTO("new name", "123456789", "ID123456");
        Mockito.when(driverRestService.updateTruckDriver(id, dto)).thenReturn(result);

        //when+then
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/drivers/" + id).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "fullName": "new name"
                        }
                                """));
        Mockito.verify(driverRestService).updateTruckDriver(id, dto);
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.fullName", equalTo(result.getFullName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tel", equalTo(result.getTel())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocument", equalTo(result.getIdDocument())));
    }

}