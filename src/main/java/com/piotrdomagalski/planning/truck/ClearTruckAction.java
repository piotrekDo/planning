package com.piotrdomagalski.planning.truck;

/**
 * Command class used to remove tautliner, driver and carreir form truck entity.
 * Used in services.
 */

public class ClearTruckAction {

    private final TruckEntity truck;

    public ClearTruckAction(TruckEntity truck) {
        this.truck = truck;
    }

    public boolean execute() {
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

            truck.getAppUser().forEach(x -> x.getFavoritesTrucks().remove(truck));
            truck.getAppUser().clear();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
