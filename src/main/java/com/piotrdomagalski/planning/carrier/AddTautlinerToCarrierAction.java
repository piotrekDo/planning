package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;

/**
 * Command class used to assign tautliner to specific carrier.
 */

class AddTautlinerToCarrierAction implements CarrierCommand {

    private CarrierEntity carrier;
    private TautlinerEntity tautliner;

    AddTautlinerToCarrierAction(CarrierEntity carrier, TautlinerEntity tautliner) {
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
