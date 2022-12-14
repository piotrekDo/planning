package com.piotrdomagalski.planning.coupling_actions;

import org.springframework.web.bind.annotation.*;

/**
 * Controller class for coupling actions within the program.
 */

@RestController
@RequestMapping("/couple")
class CouplingActionsController {

    private final CouplingActionsService couplingActionsService;

    CouplingActionsController(CouplingActionsService couplingActionsService) {
        this.couplingActionsService = couplingActionsService;
    }

    @PutMapping("/truck-driver")
    TruckDriverCouple coupleTruckWithDriver(@RequestBody TruckDriverCouple couple) {
        return couplingActionsService.coupleTruckDriver(couple);
    }

    @PutMapping("/truck-tautliner")
    TruckTautlinerCouple coupleTruckWithTautliner(@RequestBody TruckTautlinerCouple couple) {
        return couplingActionsService.coupleTruckTautliner(couple);
    }

    @PutMapping("/tautliner-carrier")
    TautlinerCarrierCouple coupleTautlinerWithCarrier(@RequestBody TautlinerCarrierCouple couple) {
        return couplingActionsService.coupleTautlinerCarrier(couple);
    }
}
