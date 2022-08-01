package com.piotrdomagalski.planning.favorites;

import com.piotrdomagalski.planning.truck.TruckInfoDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @GetMapping("/{username}")
    List<TruckInfoDTO> getUserfavoritesTruck(@PathVariable String username) {
        return favoritesService.getUsersFavoriteTrucks(username);
    }

    @PostMapping
    AddNewTruckToFavoritesDTO addTruckToFavorites(@RequestBody AddNewTruckToFavoritesDTO addTruck) {
        return favoritesService.addNewTruckToFavorites(addTruck);
    }

    @DeleteMapping
    AddNewTruckToFavoritesDTO removeTruckFromFavorites(@RequestBody AddNewTruckToFavoritesDTO removeTruck) {
        return favoritesService.removeTruckFromFavorites(removeTruck);
    }

}
