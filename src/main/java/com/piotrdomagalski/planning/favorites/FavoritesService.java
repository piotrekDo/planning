package com.piotrdomagalski.planning.favorites;

import com.piotrdomagalski.planning.app_user.AppUser;
import com.piotrdomagalski.planning.app_user.AppUserRepository;
import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckInfoDTO;
import com.piotrdomagalski.planning.truck.TruckRepository;
import com.piotrdomagalski.planning.truck.TruckTransformer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FavoritesService {

    private final AppUserRepository appUserRepository;
    private final TruckRepository truckRepository;
    private final TruckTransformer truckTransformer;

    public FavoritesService(AppUserRepository appUserRepository, TruckRepository truckRepository, TruckTransformer truckTransformer) {
        this.appUserRepository = appUserRepository;
        this.truckRepository = truckRepository;
        this.truckTransformer = truckTransformer;
    }

    List<TruckInfoDTO> getUsersFavoriteTrucks(String username) {
        List<TruckEntity> trucks = truckRepository.findByAppUser_username(username);
        return trucks.stream().map(truckTransformer::toinfoDto).collect(Collectors.toList());
    }

    AddNewTruckToFavoritesDTO addNewTruckToFavorites(AddNewTruckToFavoritesDTO newFavorites) {
        AppUser appUser = appUserRepository.findByUsernameIgnoreCase(newFavorites.getUsername()).orElseThrow(() ->
                new NoSuchElementException("No user found with username " + newFavorites.getUsername()));
        TruckEntity truck = truckRepository.findByTruckPlatesIgnoreCase(newFavorites.getTruck()).orElseThrow(() ->
                new NoSuchElementException("No truck found with plates " + newFavorites.getTruck()));
        if (appUser.getFavoritesTrucks().contains(truck))
            throw new IllegalOperationException(String.format("Truck %s is already on favorites list", truck.getTruckPlates()));
        appUser.getFavoritesTrucks().add(truck);
        appUserRepository.save(appUser);
        return newFavorites;
    }

    AddNewTruckToFavoritesDTO removeTruckFromFavorites(AddNewTruckToFavoritesDTO newFavorites) {
        AppUser appUser = appUserRepository.findByUsernameIgnoreCase(newFavorites.getUsername()).orElseThrow(() ->
                new NoSuchElementException("No user found with username " + newFavorites.getUsername()));
        TruckEntity truck = truckRepository.findByTruckPlatesIgnoreCase(newFavorites.getTruck()).orElseThrow(() ->
                new NoSuchElementException("No truck found with plates " + newFavorites.getTruck()));
        appUser.getFavoritesTrucks().remove(truck);
        appUserRepository.save(appUser);
        return newFavorites;
    }


}
