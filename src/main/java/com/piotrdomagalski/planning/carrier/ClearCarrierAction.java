package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;

import java.util.Iterator;

/**
 * Command used to clear carrier before deleting- removes all trucks, tautliners and drivers.
 * Command need mostly for tautliners- these cannot be removed along by Hibernate cascade,
 * because of organization's tautliners- these must remain within the program, so I applied iterator
 * for trucks and drivers also for coherence reason.
 */

 class ClearCarrierAction implements CarrierCommand {

    private CarrierEntity carrier;

     ClearCarrierAction(CarrierEntity carrier) {
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
                truck.getAppUser().forEach(user -> user.getFavoritesTrucks().remove(truck));
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
