package com.piotrdomagalski.planning.truck_driver;

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
