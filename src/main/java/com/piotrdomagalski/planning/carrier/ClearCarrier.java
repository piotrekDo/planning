package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.springframework.stereotype.Component;

import java.util.Iterator;

public class ClearCarrier implements CarrierCommand {

    private CarrierEntity carrier;

    public ClearCarrier(CarrierEntity carrier) {
        this.carrier = carrier;
    }

    @Override
    public boolean execute() {
        try {
            clearTruckDrivers();
            clearTautliners();
            clearTrucks();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void clearTrucks() {
        for (Iterator<TruckEntity> it = carrier.getTrucks().iterator(); it.hasNext();) {
            TruckEntity truck = it.next();
            if (truck.getCarrier() != null) {
                truck.setCarrier(null);
            }
        }
    }

    private void clearTautliners() {
        for (Iterator<TautlinerEntity> it = carrier.getTautliners().iterator(); it.hasNext();) {
            TautlinerEntity tautliner = it.next();
            if (tautliner.getTruck() != null) {
                tautliner.getTruck().setTautliner(null);
                tautliner.setTruck(null);
            }
            if (tautliner.getCarrier() != null) {
                tautliner.setCarrier(null);
            }
        }
    }

    private void clearTruckDrivers() {
        for (Iterator<TruckDriverEntity> it = carrier.getDrivers().iterator(); it.hasNext();) {
            TruckDriverEntity driver = it.next();
            if (driver.getTruck() != null) {
                driver.getTruck().setTruckDriver(null);
                driver.setTruck(null);
            }
            if (driver.getCarrier() != null) {
                driver.setCarrier(null);
            }
        }
    }
}
