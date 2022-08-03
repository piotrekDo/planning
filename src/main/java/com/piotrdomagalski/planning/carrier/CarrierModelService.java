package com.piotrdomagalski.planning.carrier;


import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerModelService;
import com.piotrdomagalski.planning.tautliner.TautlinerRepository;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckModelService;
import com.piotrdomagalski.planning.truck.TruckRepository;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverModelService;
import com.piotrdomagalski.planning.truck_driver.TruckDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.Optional;

public class CarrierModelService {
    private CarrierEntity carrier;

    public CarrierModelService(CarrierEntity carrier) {
        this.carrier = carrier;
    }

    public void setCarrier(CarrierEntity carrier) {
        this.carrier = carrier;
    }

    public boolean clearCarrier() {
        TruckDriverModelService truckDriverModelService = new TruckDriverModelService(null);
        TautlinerModelService tautlinerModelService = new TautlinerModelService(null);
        TruckModelService truckModelService = new TruckModelService(null);

        try {
            carrier.getDrivers().forEach(driver -> {
                truckDriverModelService.setDriver(driver);
                truckDriverModelService.clearDriver();
            });
            carrier.getTautliners().forEach(tautliner -> {
                tautlinerModelService.setTautliner(tautliner);
                tautlinerModelService.clearTautliner();
                if (!tautliner.getXpo()) {
                }
                carrier.getTrucks().forEach(truck -> {
                    truckModelService.setTruck(truck);
                    truckModelService.clearTruck();
                });
            });
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean addTruck(TruckEntity truck) {
        if (truck.getCarrier() != null) {
            throw new IllegalOperationException(String.format("Truck with plates: %s has carrier SAP: %s",
                    truck.getTruckPlates(), truck.getCarrier().getSap()));
        }

        try {
            carrier.getTrucks().add(truck);
            truck.setCarrier(carrier);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeTruckByPlates(String plates) {
        Optional<TruckEntity> truckToRemove = carrier.getTrucks().stream()
                .filter(truck -> truck.getTruckPlates().equals(plates))
                .findFirst();

        TruckEntity truckEntity = truckToRemove.orElseThrow(
                () -> new NoSuchElementException(String.format("Truck with plates: %s doesn't exist at carrier %s",
                        plates, carrier.getSap())));
        try {
            new TruckModelService(truckEntity).clearTruck();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addDriver(TruckDriverEntity driver) {
        if (driver.getCarrier() != null) {
            throw new IllegalOperationException(String.format("Driver with database ID: %d, has carrier SAP: %s",
                    driver.getId(), driver.getCarrier().getSap()));
        }

        try {
            driver.setCarrier(carrier);
            carrier.getDrivers().add(driver);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeDriverById(Long id) {
        Optional<TruckDriverEntity> driverToRemove = carrier.getDrivers().stream()
                .filter(driver -> driver.getId().equals(id))
                .findFirst();

        TruckDriverEntity truckDriverEntity = driverToRemove.orElseThrow(
                () -> new NoSuchElementException(String.format("Driver with id: %s doesn't exist at carrier %s",
                        id, carrier.getSap())));

        try {
            new TruckDriverModelService(truckDriverEntity).clearDriver();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addTautliner(TautlinerEntity tautliner) {
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

    public boolean removeTautlinerByPlates(String tautlinerPlates) {
        Optional<TautlinerEntity> tautlinerToRemove = carrier.getTautliners().stream()
                .filter(tautliner -> tautliner.getTautlinerPlates().equals(tautlinerPlates))
                .findFirst();

        TautlinerEntity tautlinerEntity = tautlinerToRemove.orElseThrow(
                () -> new NoSuchElementException(String.format("Tautliner with plates: %s doesn't exist", tautlinerPlates)));

        try {
            new TautlinerModelService(tautlinerEntity).clearTautliner();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
