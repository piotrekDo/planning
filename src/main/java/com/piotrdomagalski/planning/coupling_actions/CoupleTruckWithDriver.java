package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;

/**
 * Command used to couple truck with driver. Can be also used to uncouple if passing with null.
 * Will ensure that connected entity is uncoupled.
 */

class CoupleTruckWithDriver implements CoupleCommand {

    private TruckDriverEntity driver;
    private TruckEntity truck;

    CoupleTruckWithDriver(TruckDriverEntity driver, TruckEntity truck) {
        this.driver = driver;
        this.truck = truck;
    }

    @Override
    public boolean couple() {
        if (driver != null && truck != null && !driver.getCarrier().equals(truck.getCarrier()))
            throw new IllegalOperationException("Truck and driver must have the same carrier!");

        try {
            if (driver != null && driver.getTruck() != null) {
                driver.getTruck().setTruckDriver(null);
            }

            if (driver != null)
                driver.setTruck(truck);

            if (truck != null && truck.getTruckDriver() != null) {
                truck.getTruckDriver().setTruck(null);
            }

            if (truck != null)
                truck.setTruckDriver(driver);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalOperationException(String.format("Could not couple truck: %s with driver id: %s", truck.getTruckPlates(), driver.getId()));
        }
        return true;
    }

}
