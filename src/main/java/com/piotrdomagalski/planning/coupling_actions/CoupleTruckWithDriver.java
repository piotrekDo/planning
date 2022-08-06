package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;

public class CoupleTruckWithDriver  implements CoupleCommand{

    private TruckDriverEntity driver;
    private TruckEntity truck;

    public CoupleTruckWithDriver(TruckDriverEntity driver, TruckEntity truck) {
        this.driver = driver;
        this.truck = truck;
    }

    @Override
    public boolean couple() {
        if (!driver.getCarrier().getSap().equals(truck.getCarrier().getSap()))
            throw new IllegalOperationException("Truck and driver must have the same carrier!");

        try {
            if (driver.getTruck() != null) {
                driver.getTruck().setTruckDriver(null);
            }
            driver.setTruck(truck);

            if (truck.getTruckDriver() != null) {
                truck.getTruckDriver().setTruck(null);
            }
            truck.setTruckDriver(driver);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalOperationException(String.format("Could not couple truck: %s with driver id: %s", truck.getTruckPlates(), driver.getId()));
        }
        return true;
    }

}
