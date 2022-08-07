package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.truck_driver.ClearTruckDriver;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;

import java.util.NoSuchElementException;
import java.util.Optional;

public class RemoveTruckDriverFromCarrier implements CarrierCommand{

    private CarrierEntity carrier;
    private Long driverId;

    public RemoveTruckDriverFromCarrier(CarrierEntity carrier, Long driverId) {
        this.carrier = carrier;
        this.driverId = driverId;
    }

    @Override
    public boolean execute() {
        Optional<TruckDriverEntity> driverToRemove = carrier.getDrivers().stream()
                .filter(driver -> driver.getId().equals(driverId))
                .findFirst();

        TruckDriverEntity truckDriverEntity = driverToRemove.orElseThrow(
                () -> new NoSuchElementException(String.format("Driver with id: %s doesn't exist at carrier %s",
                        driverId, carrier.getSap())));

        try {
            new ClearTruckDriver(truckDriverEntity).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
