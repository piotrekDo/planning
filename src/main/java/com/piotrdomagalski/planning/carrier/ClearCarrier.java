package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.ClearTautliner;
import com.piotrdomagalski.planning.truck.ClearTruck;
import com.piotrdomagalski.planning.truck_driver.ClearTruckDriver;

public class ClearCarrier implements CarrierCommand{

    private CarrierEntity carrier;

    public ClearCarrier(CarrierEntity carrier) {
        this.carrier = carrier;
    }

    @Override
    public boolean execute() {
        ClearTruckDriver clearTruckDriver = new ClearTruckDriver(null);
        ClearTautliner clearTautliner = new ClearTautliner(null);
        ClearTruck clearTruck = new ClearTruck(null);

        try {
            carrier.getDrivers().forEach(driver -> {
                clearTruckDriver.setDriver(driver);
                clearTruckDriver.execute();
            });
            carrier.getTautliners().forEach(tautliner -> {
                clearTautliner.setTautliner(tautliner);
                clearTautliner.execute();
                if (!tautliner.getXpo()) {
                    //delete?
                }
                carrier.getTrucks().forEach(truck -> {
                    clearTruck.setTruck(truck);
                    clearTruck.execute();
                });
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
