package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.truck.TruckEntity;

/**
 * Command class used to assign truck to specific carrier.
 */

class AddTruckToCarrierAction implements CarrierCommand {

    private CarrierEntity carrier;
    private TruckEntity truck;

    AddTruckToCarrierAction(CarrierEntity carrier, TruckEntity truck) {
        this.carrier = carrier;
        this.truck = truck;
    }

    @Override
    public boolean execute() {
        if (truck.getCarrier() != null) {
            throw new IllegalOperationException(String.format("Truck with plates: %s has carrier SAP: %s",
                    truck.getTruckPlates(), truck.getCarrier().getSap()));
        }
        try {
            carrier.getTrucks().add(truck);
            truck.setCarrier(carrier);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
