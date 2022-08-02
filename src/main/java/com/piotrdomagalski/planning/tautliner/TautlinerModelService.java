package com.piotrdomagalski.planning.tautliner;

public class TautlinerModelService {

    private TautlinerEntity tautliner;

    public TautlinerModelService(TautlinerEntity tautliner) {
        this.tautliner = tautliner;
    }

    public void setTautliner(TautlinerEntity tautliner) {
        this.tautliner = tautliner;
    }

    public boolean clearTautliner() {
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
