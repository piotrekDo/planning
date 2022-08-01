package com.piotrdomagalski.planning.carrier;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for carrier entity allowing to obtain and post/put data.
 * @RestController ensures response coding to JSON format.
 */

@RestController
@RequestMapping("/carriers")
class CarrierController {

    private final CarrierService carrierService;

    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    /**
     *
     * @param page if not passed will return zeroth page.
     * @param size if not passed will return default value.
     * @return
     */

    @GetMapping
    Page<CarrierFullIDto> getAllCarriersFull(@RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer size) {
        return carrierService.getAllCarriers(page, size);
    }

    /**
     *
     * @param page if not passed will return zeroth page.
     * @param size if not passed will return default value.
     * @return
     */

    @GetMapping("/all-short")
    Page<CarrierShortInfoDTO> getAllCarriersShort(@RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer size) {
        return carrierService.getCarriersShortInfo(page, size);
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
