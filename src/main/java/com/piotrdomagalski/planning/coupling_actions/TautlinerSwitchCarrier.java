package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;

/**
 * DTO class used for coupling request for carrier and tautliner.
 */

class TautlinerSwitchCarrier implements CoupleCommand {

    private final CarrierEntity carrier;
    private final TautlinerEntity tautliner;

    TautlinerSwitchCarrier(CarrierEntity carrier, TautlinerEntity tautliner) {
        this.carrier = carrier;
        this.tautliner = tautliner;
    }

    @Override
    public boolean couple() {
        if (tautliner.getTruck() != null)
            throw new IllegalOperationException("Tautliner has truck, uncouple first");

        if (!tautliner.getXpo() && tautliner.getCarrier() != null)
            throw new IllegalOperationException("Cannot switch carrier for non-xpo trailer");

        try {
            if (tautliner.getCarrier() != null) {
                tautliner.getCarrier().getTautliners().remove(tautliner);
            }
            tautliner.setCarrier(carrier);
            carrier.getTautliners().add(tautliner);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalOperationException("Could not switch carriers");
        }
        return true;
    }
}
