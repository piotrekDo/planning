package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.truck.TruckEntity;

class AddTruckToCarrier implements CarrierCommand {

    private CarrierEntity carrier;
    private TruckEntity truck;

    AddTruckToCarrier(CarrierEntity carrier, TruckEntity truck) {
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
