package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.app_user.AppUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.equalTo;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CouplingActionsController.class)
@WithMockUser(username = "Test", authorities = {"USER"})
class CouplingActionsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AppUserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    CouplingActionsService couplingActionsService;

    @Test
    void coupleTruckWithDriver_should_return_response_200_and_response_body() throws Exception {
        //given
        TruckDriverCouple couple = new TruckDriverCouple("TRUCK1234", 9999999999999999L);
        Mockito.when(couplingActionsService.coupleTruckDriver(couple)).thenReturn(couple);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/couple/truck-driver").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "driver": 9999999999999999,
                          "truck": "TRUCK1234"
                        }
                        """));
        //then
        Mockito.verify(couplingActionsService).coupleTruckDriver(couple);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.driver", equalTo(9999999999999999L)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.truck", equalTo("TRUCK1234")));
    }

    @Test
    void coupleTruckWithDriver_should_return_not_found_if_no_driver_was_found() throws Exception {
        //given
        TruckDriverCouple couple = new TruckDriverCouple("TRUCK1234", 9999999999999999L);
        Mockito.when(couplingActionsService.coupleTruckDriver(couple)).thenThrow(
                new NoSuchElementException("No driver found with id: " + couple.getDriver()));

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/couple/truck-driver").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "driver": 9999999999999999,
                          "truck": "TRUCK1234"
                        }
                        """));

        //then
        Mockito.verify(couplingActionsService).coupleTruckDriver(couple);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No driver found with id: " + couple.getDriver())));
    }

    @Test
    void coupleTruckWithDriver_should_return_not_found_if_no_truck_was_found() throws Exception {
        //given
        TruckDriverCouple couple = new TruckDriverCouple("TRUCK1234", 9999999999999999L);
        Mockito.when(couplingActionsService.coupleTruckDriver(couple)).thenThrow(
                new NoSuchElementException("No truck found with plates: " + couple.getTruck()));

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/couple/truck-driver").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "driver": 9999999999999999,
                          "truck": "TRUCK1234"
                        }
                        """));

        //then
        Mockito.verify(couplingActionsService).coupleTruckDriver(couple);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No truck found with plates: " + couple.getTruck())));
    }

}