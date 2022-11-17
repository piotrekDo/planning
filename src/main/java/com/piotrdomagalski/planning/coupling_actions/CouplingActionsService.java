package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import com.piotrdomagalski.planning.logs.LogsService;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerRepository;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckRepository;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Service class for coupling actions.
 */

@Service
class CouplingActionsService {

    private final CouplingActions couplingActions;
    private final CarrierRepository carrierRepository;
    private final TruckDriverRepository truckDriverRepository;
    private final TruckRepository truckRepository;
    private final TautlinerRepository tautlinerRepository;
    private final LogsService logsService;


    public CouplingActionsService(CouplingActions couplingActions, CarrierRepository carrierRepository, TruckDriverRepository truckDriverRepository, TruckRepository truckRepository, TautlinerRepository tautlinerRepository, LogsService logsService) {
        this.couplingActions = couplingActions;
        this.carrierRepository = carrierRepository;
        this.truckDriverRepository = truckDriverRepository;
        this.truckRepository = truckRepository;
        this.tautlinerRepository = tautlinerRepository;
        this.logsService = logsService;
    }

    TruckDriverCouple coupleTruckDriver(TruckDriverCouple couple) {
        TruckDriverEntity driver = couple.getDriver() != null ? truckDriverRepository.findById(couple.getDriver()).orElseThrow(() ->
                new NoSuchElementException("No driver found with id: " + couple.getDriver())) : null;
        TruckEntity truck = couple.getTruck() != null ? truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck()).orElseThrow(() ->
                new NoSuchElementException("No truck found with plates: " + couple.getTruck())) : null;

        logTruckDriverChanges(truck, driver);
        couplingActions.coupleTruckWithDriver(driver, truck);
        if (driver != null)
            truckDriverRepository.save(driver);
        if (truck != null)
            truckRepository.save(truck);

        return couple;
    }

    TruckTautlinerCouple coupleTruckTautliner(TruckTautlinerCouple couple) {
        TruckEntity truck = couple.getTruck() != null ? truckRepository.findByTruckPlatesIgnoreCase(couple.getTruck()).orElseThrow(() ->
                new NoSuchElementException("No truck found with plates: " + couple.getTruck())) : null;

        if (truck != null && truck.getTautliner() != null && couple.getTautliner() != null && truck.getTautliner().getTautlinerPlates().equalsIgnoreCase(couple.getTautliner()))
            return couple;

        TautlinerEntity tautliner = couple.getTautliner() != null ? tautlinerRepository.findByTautlinerPlatesIgnoreCase(couple.getTautliner()).orElseThrow(() ->
                new NoSuchElementException("No tautliner found with plates: " + couple.getTautliner())) : null;

        logTruckTautlinerChanges(truck, tautliner);
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

    private void logTruckDriverChanges(TruckEntity truck, TruckDriverEntity driver) {
        String truckPlates = truck != null ? truck.getTruckPlates() : null;
        Long driversId = driver != null ? driver.getId() : null;
        Long currentDriver = truck != null && truck.getTruckDriver() != null ? truck.getTruckDriver().getId() : null;
        String currentTruck = driver != null && driver.getTruck() != null ? driver.getTruck().getTruckPlates() : null;

        if (driversId != null && currentDriver != null && driversId.equals(currentDriver))
            return;

        String driverData = driver != null ? driver.getFullName() + " " + driver.getIdDocument() : null;
        String currentDriverData = currentDriver != null ? truck.getTruckDriver().getFullName() + " " + truck.getTruckDriver().getIdDocument() : null;
        if (truckPlates != null) {
            if (driversId != null)
                logsService.createCoupleLog(truckPlates, driverData);
            else
                logsService.createUnCoupleLog(truckPlates, currentDriverData);
        }
        if (driversId != null) {
            if (truckPlates != null)
                logsService.createCoupleLog(driverData, truckPlates);
            else logsService.createUnCoupleLog(driverData, currentTruck);
        }
        if (currentDriver != null)
            logsService.createUnCoupleLog(currentDriverData, truckPlates);
        if (currentTruck != null)
            logsService.createUnCoupleLog(currentTruck, driverData);
    }

    private void logTruckTautlinerChanges(TruckEntity truck, TautlinerEntity tautliner) {
        String truckPlates = truck != null ? truck.getTruckPlates() : null;
        String tautlinerPlates = tautliner != null ? tautliner.getTautlinerPlates() : null;
        String currentTautliner = truck != null && truck.getTautliner() != null ? truck.getTautliner().getTautlinerPlates() : null;
        String currentTruck = tautliner != null && tautliner.getTruck() != null ? tautliner.getTruck().getTruckPlates() : null;

        if (tautlinerPlates != null && currentTautliner != null && tautlinerPlates.equals(currentTautliner))
            return;

        if (truckPlates != null) {
            if (tautlinerPlates != null)
                logsService.createCoupleLog(truckPlates, tautlinerPlates);
            else
                logsService.createUnCoupleLog(truckPlates, currentTautliner);
        }
        if (tautlinerPlates != null) {
            if (truckPlates != null)
                logsService.createCoupleLog(tautlinerPlates, truckPlates);
            else
                logsService.createUnCoupleLog(tautlinerPlates, currentTruck);
        }
        if (currentTautliner != null)
            logsService.createUnCoupleLog(currentTautliner, truckPlates);
        if (currentTruck != null)
            logsService.createUnCoupleLog(currentTruck, tautlinerPlates);
    }

    private CarrierEntity getCarrier(TautlinerCarrierCouple couple) {
        return carrierRepository.findBySap(couple.getCarrierSap()).orElseThrow(() ->
                new NoSuchElementException("No carrier found with id: " + couple.getCarrierSap()));
    }

}
