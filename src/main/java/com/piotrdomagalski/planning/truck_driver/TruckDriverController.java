package com.piotrdomagalski.planning.truck_driver;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
class TruckDriverController {

    private final TruckDriverRestService driverRestService;

    TruckDriverController(@Qualifier("truckDriverRest") TruckDriverRestService driverRestService) {
        this.driverRestService = driverRestService;
    }

    @GetMapping
    List<TruckDriverEntity> getAllDrivers() {
        return driverRestService.getAllTruckDrivers();
    }

    @GetMapping("/{id}")
    TruckDriverInfoDTO getDriverById(@PathVariable Long id) {
        return driverRestService.getTruckDriverById(id);
    }

    @PostMapping("/{carrierId}")
    TruckDriverNewUpdateDTO addNewDriver(@PathVariable Long carrierId, @RequestBody @Validated(value = AddDriver.class) TruckDriverNewUpdateDTO driver) {
        return driverRestService.addNewDriver(carrierId, driver);
    }

    @DeleteMapping("/{id}")
    TruckDriverEntity deleteDriverById(@PathVariable Long id) {
        return driverRestService.deleteTruckDriverById(id);
    }

    @PutMapping("/{id}")
    TruckDriverNewUpdateDTO updateDriver(@PathVariable Long id, @RequestBody @Validated(value = UpdateDriver.class) TruckDriverNewUpdateDTO driverDto) {
        return driverRestService.updateTruckDriver(id, driverDto);
    }
}
