package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.springframework.stereotype.Component;

/**
 * Injectable library of all coupling actions possible, used in services.
 */

@Component
class CouplingActions {

    private CoupleCommand coupleCommand;

    void setCoupleCommand(CoupleCommand coupleCommand) {
        this.coupleCommand = coupleCommand;
    }

    void coupleTruckWithDriver(TruckDriverEntity driver, TruckEntity truck) {
        setCoupleCommand(new CoupleTruckWithDriver(driver, truck));
        coupleCommand.couple();
    }

    void coupleTruckWithTautliner(TruckEntity truck, TautlinerEntity tautliner) {
        setCoupleCommand(new CoupleTruckWithTautliner(truck, tautliner));
        coupleCommand.couple();
    }

    void switchTautlinerCarrier(CarrierEntity carrier, TautlinerEntity tautliner) {
        setCoupleCommand(new TautlinerSwitchCarrier(carrier, tautliner));
        coupleCommand.couple();
    }
}
