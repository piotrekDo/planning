package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;

class AddTautlinerToCarrier implements CarrierCommand {

    private CarrierEntity carrier;
    private TautlinerEntity tautliner;

    AddTautlinerToCarrier(CarrierEntity carrier, TautlinerEntity tautliner) {
        this.carrier = carrier;
        this.tautliner = tautliner;
    }

    @Override
    public boolean execute() {
        if (tautliner.getCarrier() != null) {
            throw new IllegalOperationException(String.format("Tautliner with plates: %s, has carrier SAP: %s",
                    tautliner.getTautlinerPlates(), tautliner.getCarrier().getSap()));
        }

        try {
            carrier.getTautliners().add(tautliner);
            tautliner.setCarrier(carrier);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
