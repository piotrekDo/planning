package com.piotrdomagalski.planning.truck;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trucks")
class TruckController {

    private final TruckService truckService;

    TruckController(TruckService truckService) {
        this.truckService = truckService;
    }

    @GetMapping
    Page<TruckInfoDTO> getAllTrucks(@RequestParam(required = false) Integer page,
                                    @RequestParam(required = false) Integer size) {
        return truckService.getAllTrucks(page, size);
    }

    @GetMapping("/{plates}")
    TruckInfoDTO getTruckByPlates(@PathVariable String plates) {
        return truckService.getTruckByPlates(plates);
    }

    @PostMapping("/{carrierSap}")
    TruckInfoDTO addNewTruck(@PathVariable String carrierSap, @RequestBody @Validated(value = AddTruck.class) TruckNewUpdateDTO truck) {
        return truckService.addNewTruck(carrierSap, truck);
    }

    @DeleteMapping("/{plates}")
    TruckEntity deleteTruckByPlates(@PathVariable String plates) {
        return truckService.deleteTruckByPlates(plates);
    }

    @PutMapping("/{plates}")
    TruckNewUpdateDTO updateTruck(@PathVariable String plates, @RequestBody @Validated(value = UpdateTruck.class) TruckNewUpdateDTO dto) {
        return truckService.updateTruck(plates, dto);
    }
}
