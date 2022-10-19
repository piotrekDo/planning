package com.piotrdomagalski.planning.favorites;

import com.piotrdomagalski.planning.app_user.AppUserService;
import com.piotrdomagalski.planning.truck.TruckInfoDTO;
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

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FavoritesController.class)
@WithMockUser(username = "Test", authorities = {"USER"})
class FavoritesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FavoritesService favoritesService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    AppUserService appUserService;

    @Test
    void getUsersFavoriteTrucks_should_respond_with_code_200_and_favorites_trucks() throws Exception {
        //given
        String username = "user";
        TruckInfoDTO truckInfoDTO = new TruckInfoDTO("ABC123", false, null, null, null, null, null, null, null, null, null);
        TruckInfoDTO truck2InfoDTO = new TruckInfoDTO("XYZ456", true, null, null, null, null, null, null, null, null, null);
        Mockito.when(favoritesService.getUsersFavoriteTrucks(username)).thenReturn(List.of(truckInfoDTO, truck2InfoDTO));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/favorites/" + username).contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].truckPlates", equalTo("ABC123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].truckPlates", equalTo("XYZ456")));
    }

    @Test
    void addNewTruckToFavorites_should_respond_with_code_200_and_add_truck_to_favorites() throws Exception {
        AddNewTruckToFavoritesDTO request = new AddNewTruckToFavoritesDTO("user", "ABC123");
        Mockito.when(favoritesService.addNewTruckToFavorites(request)).thenReturn(request);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/favorites").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "user",
                          "truck": "ABC123"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("user")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.truck", equalTo("ABC123")));

    }

    @Test
    void removeTruckFromFavorites_should_respond_with_code_200_and_remove_truck_from_favorites() throws Exception {
        AddNewTruckToFavoritesDTO request = new AddNewTruckToFavoritesDTO("user", "ABC123");
        Mockito.when(favoritesService.removeTruckFromFavorites(request)).thenReturn(request);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/favorites").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "user",
                          "truck": "ABC123"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("user")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.truck", equalTo("ABC123")));

    }

}