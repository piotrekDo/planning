package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;

/**
 * Command class used to assign driver to specific carrier.
 */

class AddDriverToCarrierAction implements CarrierCommand {

    private CarrierEntity carrier;
    private TruckDriverEntity driver;

    AddDriverToCarrierAction(CarrierEntity carrier, TruckDriverEntity driver) {
        this.carrier = carrier;
        this.driver = driver;
    }

    @Override
    public boolean execute() {
        if (driver.getCarrier() != null) {
            throw new IllegalOperationException(String.format("Driver with database ID: %d, has carrier SAP: %s",
                    driver.getId(), driver.getCarrier().getSap()));
        }

        try {
            driver.setCarrier(carrier);
            carrier.getDrivers().add(driver);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
