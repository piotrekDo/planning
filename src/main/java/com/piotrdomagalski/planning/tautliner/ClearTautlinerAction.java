package com.piotrdomagalski.planning.tautliner;

/**
 * Command class used for uncouple tautliner from carrier and truck.
 */

public class ClearTautlinerAction {

    private TautlinerEntity tautliner;

    public ClearTautlinerAction(TautlinerEntity tautliner) {
        this.tautliner = tautliner;
    }

    public boolean execute() {
        try {
            if (tautliner.getTruck() != null) {
                tautliner.getTruck().setTautliner(null);
                tautliner.setTruck(null);
            }
            if (tautliner.getCarrier() != null) {
                tautliner.getCarrier().getTautliners().remove(tautliner);
                tautliner.setCarrier(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
