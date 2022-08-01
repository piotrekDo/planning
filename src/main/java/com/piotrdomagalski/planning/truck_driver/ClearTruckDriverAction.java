package com.piotrdomagalski.planning.truck_driver;

/**
 * Command class used to remove truck and carrier form driver entity.
 * Used in services.
 */

public class ClearTruckDriverAction {

    private TruckDriverEntity driver;

    public ClearTruckDriverAction(TruckDriverEntity driver) {
        this.driver = driver;
    }

    public boolean execute() {
        try {
            if (driver.getTruck() != null) {
                driver.getTruck().setTruckDriver(null);
                driver.setTruck(null);
            }
            if (driver.getCarrier() != null) {
                driver.getCarrier().getDrivers().remove(driver);
                driver.setCarrier(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
