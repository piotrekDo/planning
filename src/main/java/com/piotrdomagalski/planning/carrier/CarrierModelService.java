package com.piotrdomagalski.planning.carrier;


import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckModelService;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;

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

    public TruckEntity removeTruck(Long id) {
        Optional<TruckEntity> truckToRemove = carrier.getTrucks().stream()
                .filter(truck -> truck.getId().equals(id))
                .findFirst();

        TruckEntity truckEntity = truckToRemove.orElseThrow(
                () -> new NoSuchElementException("Truck doesn't exist"));

        new TruckModelService(truckEntity).clearTruck();

        carrier.getTrucks().remove(truckEntity);
        return truckEntity;
    }

    public boolean addDriver(TruckDriverEntity driver) {
        if (driver.getCarrier() != null) {
            throw new IllegalOperationException(String.format("Driver with database ID: %d, has carrier SAP: %s",
                    driver.getId(), driver.getCarrier().getSap()));
        }

        try {
            carrier.getDrivers().add(driver);
            driver.setCarrier(carrier);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public TruckDriverEntity removeDriver(Long id) {
        Optional<TruckDriverEntity> driverToRemove = carrier.getDrivers().stream()
                .filter(driver -> driver.getId().equals(id))
                .findFirst();

        TruckDriverEntity truckDriverEntity = driverToRemove.orElseThrow(
                () -> new NoSuchElementException("Driver doesn't exist"));

        if (truckDriverEntity.getTruck() != null) {
            truckDriverEntity.getTruck().setTruckDriver(null);
            truckDriverEntity.setTruck(null);
        }

        truckDriverEntity.setCarrier(null);
        carrier.getDrivers().remove(truckDriverEntity);
        return truckDriverEntity;
    }

}
