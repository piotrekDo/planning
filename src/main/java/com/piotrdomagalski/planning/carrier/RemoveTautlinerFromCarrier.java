package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.ClearTautliner;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;

import java.util.NoSuchElementException;
import java.util.Optional;

class RemoveTautlinerFromCarrier implements CarrierCommand {

    private CarrierEntity carrier;
    private String tautlinerPlates;

    RemoveTautlinerFromCarrier(CarrierEntity carrier, String tautlinerPlates) {
        this.carrier = carrier;
        this.tautlinerPlates = tautlinerPlates;
    }

    @Override
    public boolean execute() {
        Optional<TautlinerEntity> tautlinerToRemove = carrier.getTautliners().stream()
                .filter(tautliner -> tautliner.getTautlinerPlates().equals(tautlinerPlates))
                .findFirst();

        TautlinerEntity tautlinerEntity = tautlinerToRemove.orElseThrow(
                () -> new NoSuchElementException(String.format("Tautliner with plates: %s doesn't exist", tautlinerPlates)));

        try {
            new ClearTautliner(tautlinerEntity).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
