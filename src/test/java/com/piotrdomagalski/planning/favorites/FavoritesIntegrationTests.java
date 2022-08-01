package com.piotrdomagalski.planning.favorites;

import com.piotrdomagalski.planning.PlanningApplication;
import com.piotrdomagalski.planning.app_user.AppUser;
import com.piotrdomagalski.planning.app_user.AppUserRepository;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PlanningApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WithMockUser(username = "Test", authorities = {"USER"})
public class FavoritesIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    TruckRepository truckRepository;

    @Test
    void adding_truck_to_favorites_should_return_code_200_and_add_truck_to_favorites_list() throws Exception {
        //given
        AppUser appUser = new AppUser("user", "user@user.com", "pass");
        TruckEntity truck = TruckEntity.newTruck("ABC123", false);
        appUserRepository.save(appUser);
        truckRepository.save(truck);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/favorites").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "user",
                          "truck": "ABC123"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("user")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.truck", equalTo("ABC123")));
        List<TruckEntity> userFavorites = truckRepository.findByAppUser_username("user");
        assertEquals(userFavorites.size(), 1);
        assertEquals(userFavorites.get(0), truck);
    }

    @Test
    void removing_truck_from_favorites_should_return_code_200_and_remove_truck_from_favorites_list() throws Exception {
        //given
        AppUser appUser = new AppUser("user", "user@user.com", "pass");
        TruckEntity truck = TruckEntity.newTruck("ABC123", false);
        appUserRepository.save(appUser);
        truckRepository.save(truck);
        appUser.getFavoritesTrucks().add(truck);
        appUserRepository.save(appUser);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/favorites").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "username": "user",
                           "truck": "ABC123"
                         }
                         """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("user")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.truck", equalTo("ABC123")));
        List<TruckEntity> userFavorites = truckRepository.findByAppUser_username("user");
        assertEquals(userFavorites.size(), 0);
    }

    @Test
    void getting_users_favorite_trucks_should_return_code_200_and_list_of_trucks() throws Exception {
        //given
        AppUser appUser = new AppUser("user", "user@user.com", "pass");
        TruckEntity truck = TruckEntity.newTruck("ABC123", false);
        TruckEntity truck2 = TruckEntity.newTruck("XYZ123", true);
        appUserRepository.save(appUser);
        truckRepository.save(truck);
        truckRepository.save(truck2);
        appUser.getFavoritesTrucks().add(truck);
        appUser.getFavoritesTrucks().add(truck2);
        appUserRepository.save(appUser);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/favorites/user").contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].truckPlates", equalTo("ABC123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].truckPlates", equalTo("XYZ123")));
        List<TruckEntity> userFavorites = truckRepository.findByAppUser_username("user");
        assertEquals(userFavorites.size(), 2);
    }
}
