package com.piotrdomagalski.planning.truck;

import org.springframework.stereotype.Service;

public class TruckModelService {

    private TruckEntity truck;

    public TruckModelService(TruckEntity truck) {
        this.truck = truck;
    }

    public void setTruck(TruckEntity truck) {
        this.truck = truck;
    }

    public boolean clearTruck() {
        try {
            if (truck.getTautliner() != null) {
                truck.getTautliner().setTruck(null);
                truck.setTautliner(null);
            }
            if (truck.getTruckDriver() != null) {
                truck.getTruckDriver().setTruck(null);
                truck.setTruckDriver(null);
            }
            if (truck.getCarrier() != null) {
                truck.getCarrier().getTrucks().remove(truck);
                truck.setCarrier(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
