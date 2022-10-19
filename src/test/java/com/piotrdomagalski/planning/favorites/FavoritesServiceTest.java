package com.piotrdomagalski.planning.favorites;

import com.piotrdomagalski.planning.app_user.AppUser;
import com.piotrdomagalski.planning.app_user.AppUserRepository;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckInfoDTO;
import com.piotrdomagalski.planning.truck.TruckRepository;
import com.piotrdomagalski.planning.truck.TruckTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class FavoritesServiceTest {

    @TestConfiguration
    static class FavoritesTestConfiguration {
        @Bean
        FavoritesService favoritesService(AppUserRepository appUserRepository, TruckRepository truckRepository, TruckTransformer truckTransformer) {
            return new FavoritesService(appUserRepository, truckRepository, truckTransformer);
        }
    }

    @Autowired
    FavoritesService favoritesService;

    @MockBean
    AppUserRepository appUserRepository;

    @MockBean
    TruckRepository truckRepository;

    @MockBean
    TruckTransformer truckTransformer;

    @Test
    void getUsersFavoriteTrucks_should_return_list_of_users_trucks_dto() {
        //given
        AppUser appUser = new AppUser("user", "user@exapmple.com", "password");
        TruckEntity truck = TruckEntity.newTruck("ABC123", true);
        TruckEntity truck2 = TruckEntity.newTruck("ABC321", true);
        TruckInfoDTO truckInfoDTO = new TruckInfoDTO(truck.getTruckPlates(), truck.getMega(), null, null, null, null, null, null, null, null, null);
        TruckInfoDTO truck2InfoDTO = new TruckInfoDTO(truck2.getTruckPlates(), truck2.getMega(), null, null, null, null, null, null, null, null, null);
        appUser.getFavoritesTrucks().add(truck);
        appUser.getFavoritesTrucks().add(truck2);
        Mockito.when(truckRepository.findByAppUser_username(appUser.getUsername())).thenReturn(List.of(truck, truck2));
        Mockito.when(truckTransformer.toinfoDto(truck)).thenReturn(truckInfoDTO);
        Mockito.when(truckTransformer.toinfoDto(truck2)).thenReturn(truck2InfoDTO);

        //when
        List<TruckInfoDTO> result = favoritesService.getUsersFavoriteTrucks("user");

        //then
        assertEquals(List.of(truckInfoDTO, truck2InfoDTO), result);
    }

    @Test
    void adding_new_truck_to_favorites_should_return_added_truck_and_add_truck_to_favorites_list() {
        //given
        AppUser appUser = new AppUser("user", "user@exapmple.com", "password");
        TruckEntity truck = TruckEntity.newTruck("ABC123", true);
        AddNewTruckToFavoritesDTO addNewTruckToFavoritesDTO = new AddNewTruckToFavoritesDTO("user", "ABC123");
        Mockito.when(appUserRepository.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(appUser));
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(truck.getTruckPlates())).thenReturn(Optional.of(truck));
        Mockito.when(appUserRepository.save(appUser)).thenReturn(appUser);

        //when
        AddNewTruckToFavoritesDTO result = favoritesService.addNewTruckToFavorites(addNewTruckToFavoritesDTO);

        //then
        assertEquals(addNewTruckToFavoritesDTO, result);
        assertEquals(appUser.getFavoritesTrucks().size(), 1);
        assertEquals(appUser.getFavoritesTrucks().get(0), truck);
    }

    @Test
    void remove_from_favorites_should_return_removed_truck_and_remove_truck_from_favorites_list() {
        AppUser appUser = new AppUser("user", "user@exapmple.com", "password");
        TruckEntity truck = TruckEntity.newTruck("ABC123", true);
        appUser.getFavoritesTrucks().add(truck);
        AddNewTruckToFavoritesDTO addNewTruckToFavoritesDTO = new AddNewTruckToFavoritesDTO("user", "ABC123");
        Mockito.when(appUserRepository.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(appUser));
        Mockito.when(truckRepository.findByTruckPlatesIgnoreCase(truck.getTruckPlates())).thenReturn(Optional.of(truck));
        Mockito.when(appUserRepository.save(appUser)).thenReturn(appUser);

        //when
        AddNewTruckToFavoritesDTO result = favoritesService.removeTruckFromFavorites(addNewTruckToFavoritesDTO);
        assertEquals(addNewTruckToFavoritesDTO, result);
        assertEquals(appUser.getFavoritesTrucks().size(), 0);
    }


}