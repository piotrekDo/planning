package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.truck.ClearTruck;
import com.piotrdomagalski.planning.truck.TruckEntity;

import java.util.NoSuchElementException;
import java.util.Optional;

public class RemoveTruckFromCarrier implements CarrierCommand{

    private CarrierEntity carrier;
    private String truckPlates;

    public RemoveTruckFromCarrier(CarrierEntity carrier, String truckPlates) {
        this.carrier = carrier;
        this.truckPlates = truckPlates;
    }

    @Override
    public boolean execute() {
        Optional<TruckEntity> truckToRemove = carrier.getTrucks().stream()
                .filter(truck -> truck.getTruckPlates().equals(truckPlates))
                .findFirst();

        TruckEntity truckEntity = truckToRemove.orElseThrow(
                () -> new NoSuchElementException(String.format("Truck with plates: %s doesn't exist at carrier %s",
                        truckPlates, carrier.getSap())));
        try {
            new ClearTruck(truckEntity).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
