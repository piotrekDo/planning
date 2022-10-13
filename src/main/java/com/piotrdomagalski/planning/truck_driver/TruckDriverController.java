package com.piotrdomagalski.planning.truck_driver;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for driver entity.
 */

@RestController
@RequestMapping("/drivers")
class TruckDriverController {

    private final TruckDriverService driverService;

    TruckDriverController(@Qualifier("truckDriverRest") TruckDriverService driverService) {
        this.driverService = driverService;
    }

    /**
     * @param page if not passed will return zeroth page.
     * @param size if not passed will return default value.
     * @return
     */

    @GetMapping
    Page<TruckDriverInfoDTO> getAllDrivers(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
        return driverService.getAllTruckDrivers(page, size);
    }

    @GetMapping("/{id}")
    TruckDriverInfoDTO getDriverById(@PathVariable Long id) {
        return driverService.getTruckDriverById(id);
    }

    @PostMapping("/{carrierSap}")
    TruckDriverNewUpdateDTO addNewDriver(@PathVariable String carrierSap, @RequestBody @Validated(value = AddDriver.class) TruckDriverNewUpdateDTO driver) {
        return driverService.addNewDriver(carrierSap, driver);
    }

    @DeleteMapping("/{id}")
    TruckDriverEntity deleteDriverById(@PathVariable Long id) {
        return driverService.deleteTruckDriverById(id);
    }

    @PutMapping("/{id}")
    TruckDriverNewUpdateDTO updateDriver(@PathVariable Long id, @RequestBody @Validated(value = UpdateDriver.class) TruckDriverNewUpdateDTO driverDto) {
        return driverService.updateTruckDriver(id, driverDto);
    }
}
