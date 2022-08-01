package com.piotrdomagalski.planning.truck;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trucks")
class TruckController {

    private final TruckRestService truckRestService;

    TruckController(TruckRestService truckRestService) {
        this.truckRestService = truckRestService;
    }

    @GetMapping
    List<TruckInfoDTO> getAllTrucks() {
        return truckRestService.getAllTrucks();
    }

    @GetMapping("/{plates}")
    TruckInfoDTO getTruckByPlates(@PathVariable String plates) {
        return truckRestService.getTruckByPlates(plates);
    }

    @PostMapping("/{carrierId}")
    TruckInfoDTO addNewTruck(@PathVariable Long carrierId, @RequestBody @Validated(value = AddTruck.class) TruckNewUpdateDTO truck) {
        return truckRestService.addNewTruck(carrierId, truck);
    }

    @DeleteMapping("/{plates}")
    TruckEntity deleteTruckByPlates(@PathVariable String plates) {
        return truckRestService.deleteTruckByPlates(plates);
    }

    @PutMapping("/{plates}")
    TruckNewUpdateDTO updateTruck(@PathVariable String plates, @RequestBody @Validated(value = UpdateTruck.class) TruckNewUpdateDTO dto) {
        return truckRestService.updateTruck(plates, dto);
    }
}
