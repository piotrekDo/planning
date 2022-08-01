package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;

class CoupleTruckWithTautliner implements CoupleCommand {

    private final TruckEntity truck;
    private final TautlinerEntity tautliner;

    CoupleTruckWithTautliner(TruckEntity truck, TautlinerEntity tautliner) {
        this.truck = truck;
        this.tautliner = tautliner;
    }

    @Override
    public boolean couple() {
        CarrierEntity tautCarrier = tautliner.getCarrier();
        CarrierEntity truckCarrier = truck.getCarrier();
        if (!tautliner.getXpo() && tautCarrier != null && !tautCarrier.getSap().equals(truckCarrier.getSap()))
            throw new IllegalOperationException("Cant couple non-xpo tautliner  with another carrier's truck");

        if (tautliner.getXpo() && tautliner.getTruck() != null && tautCarrier != null && !tautCarrier.getSap().equals(truckCarrier.getSap()))
            throw new IllegalOperationException("Cant couple xpo tautliner from another carrier if it has truck assigned, clear first");

        try {
            if (tautliner.getTruck() != null) {
                tautliner.getTruck().setTautliner(null);
            }
            tautliner.setTruck(truck);

            if (tautCarrier != truckCarrier) {
                if (tautCarrier != null)
                    tautliner.getCarrier().getTautliners().remove(tautliner);
                tautliner.setCarrier(truckCarrier);
                truckCarrier.getTautliners().add(tautliner);
            }

            if (truck.getTautliner() != null) {
                truck.getTautliner().setTruck(null);
            }
            truck.setTautliner(tautliner);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalOperationException(String.format("Could not couple truck: %s with tautliner %s", truck.getTruckPlates(), tautCarrier.getTautliners()));
        }

        return true;
    }
}
