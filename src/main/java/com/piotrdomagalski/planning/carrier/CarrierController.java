package com.piotrdomagalski.planning.carrier;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carriers")
class CarrierController {

    private final CarrierRestService carrierRestService;

    public CarrierController(CarrierRestService carrierRestService) {
        this.carrierRestService = carrierRestService;
    }

    @GetMapping
    List<CarrierFullIDto> getAllCarriersFull() {
        return carrierRestService.getAllCarriers();
    }

    @GetMapping("/all-short")
    List<CarrierShortInfoDTO> getAllCarriersShort() {
        return carrierRestService.getCarriersShortInfo();
    }

    @GetMapping("/{sap}")
    CarrierFullIDto getCarrierBySap(@PathVariable String sap) {
        return carrierRestService.getCarrierBySap(sap);
    }

    @PostMapping
    CarrierNewUpdateDTO addNewCarrier(@RequestBody @Validated(value = AddCarrier.class) CarrierNewUpdateDTO carrier) {
        return carrierRestService.addNewCarrier(carrier);
    }

    @DeleteMapping("/{sap}")
    CarrierShortInfoDTO deleteCarrierBySap(@PathVariable String sap) {
        return carrierRestService.deleteCarrierBySap(sap);
    }

    @PutMapping("/{sap}")
    CarrierNewUpdateDTO updateCarrierBySap(@PathVariable String sap, @RequestBody @Validated(value = UpdateCarrier.class) CarrierNewUpdateDTO carrier) {
        return carrierRestService.updateCarrier(sap, carrier);
    }
}
