package com.piotrdomagalski.planning.truck_driver;

public class TruckDriverModelService {

    private TruckDriverEntity driver;

    public TruckDriverModelService(TruckDriverEntity driver) {
        this.driver = driver;
    }

    public void setDriver(TruckDriverEntity driver) {
        this.driver = driver;
    }

    public boolean clearDriver() {
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
