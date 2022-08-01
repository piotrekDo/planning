package com.piotrdomagalski.planning.carrier;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carriers")
class CarrierController {

    private final CarrierService carrierService;

    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @GetMapping
    List<CarrierFullIDto> getAllCarriersFull() {
        return carrierService.getAllCarriers();
    }

    @GetMapping("/all-short")
    List<CarrierShortInfoDTO> getAllCarriersShort() {
        return carrierService.getCarriersShortInfo();
    }

    @GetMapping("/{sap}")
    CarrierFullIDto getCarrierBySap(@PathVariable String sap) {
        return carrierService.getCarrierBySap(sap);
    }

    @PostMapping
    CarrierNewUpdateDTO addNewCarrier(@RequestBody @Validated(value = AddCarrier.class) CarrierNewUpdateDTO carrier) {
        return carrierService.addNewCarrier(carrier);
    }

    @DeleteMapping("/{sap}")
    CarrierShortInfoDTO deleteCarrierBySap(@PathVariable String sap) {
        return carrierService.deleteCarrierBySap(sap);
    }

    @PutMapping("/{sap}")
    CarrierNewUpdateDTO updateCarrierBySap(@PathVariable String sap, @RequestBody @Validated(value = UpdateCarrier.class) CarrierNewUpdateDTO carrier) {
        return carrierService.updateCarrier(sap, carrier);
    }
}
