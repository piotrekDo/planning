package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerRepository;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckRepository;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
class CouplingActionsService {

    private final CouplingActions couplingActions;
    private final CarrierRepository carrierRepository;
    private final TruckDriverRepository truckDriverRepository;
    private final TruckRepository truckRepository;
    private final TautlinerRepository tautlinerRepository;


    CouplingActionsService(CouplingActions couplingActions, CarrierRepository carrierRepository,
                           TruckDriverRepository truckDriverRepository, TruckRepository truckRepository, TautlinerRepository tautlinerRepository) {
        this.couplingActions = couplingActions;
        this.carrierRepository = carrierRepository;
        this.truckDriverRepository = truckDriverRepository;
        this.truckRepository = truckRepository;
        this.tautlinerRepository = tautlinerRepository;
    }

    TruckDriverCouple coupleTruckDriver(TruckDriverCouple couple) {
        TruckDriverEntity driver = null;
        TruckEntity truck = null;

        if (couple.getDriver() != null) {
            driver = truckDriverRepository.findById(couple.getDriver()).orElseThrow(() ->
                    new NoSuchElementException("No driver found with id: " + couple.getDriver()));
        }
        if (couple.getTruck() != null) {
            truck = truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck()).orElseThrow(() ->
                    new NoSuchElementException("No truck found with plates: " + couple.getTruck()));
        }

        couplingActions.coupleTruckWithDriver(driver, truck);

        if (driver != null)
            truckDriverRepository.save(driver);
        if (truck != null)
            truckRepository.save(truck);

        return couple;
    }

    TruckTautlinerCouple coupleTruckTautliner(TruckTautlinerCouple couple) {
        TruckEntity truck = null;
        TautlinerEntity tautliner = null;

        if (couple.getTruck() != null) {
            truck = truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck()).orElseThrow(() ->
                    new NoSuchElementException("No truck found with plates: " + couple.getTruck()));
        }
        if (couple.getTautliner() != null) {
            tautliner = tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner()).orElseThrow(() ->
                    new NoSuchElementException("No tautliner found with plates: " + couple.getTautliner()));
        }

        couplingActions.coupleTruckWithTautliner(truck, tautliner);

        if (truck != null)
            truckRepository.save(truck);
        if (tautliner != null)
            tautlinerRepository.save(tautliner);
        return couple;
    }

    TautlinerCarrierCouple coupleTautlinerCarrier(TautlinerCarrierCouple couple) {
        TautlinerEntity tautliner = tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner()).orElseThrow(() ->
                new NoSuchElementException("No tautliner found with plates: " + couple.getTautliner()));
        CarrierEntity carrier = tautliner.getXpo() && couple.getCarrierSap() == null ? null : getCarrier(couple);
        couplingActions.switchTautlinerCarrier(carrier, tautliner);
        tautlinerRepository.save(tautliner);
        return couple;
    }

    private CarrierEntity getCarrier(TautlinerCarrierCouple couple) {
        return carrierRepository.findBySap(couple.getCarrierSap()).orElseThrow(() ->
                new NoSuchElementException("No carrier found with id: " + couple.getCarrierSap()));
    }

}
