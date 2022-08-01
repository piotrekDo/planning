package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.springframework.stereotype.Service;

@Service
public class CarrierOperations {

    public boolean clear(CarrierEntity carrier){
        return new ClearCarrier(carrier).execute();
    }

    public boolean addDriver(CarrierEntity carrier, TruckDriverEntity driver){
        return new AddDriverToCarrier(carrier, driver).execute();
    }

    public boolean removeDriver(CarrierEntity carrier, Long driverId){
        return new RemoveTruckDriverFromCarrier(carrier, driverId).execute();
    }

    public boolean addTruck(CarrierEntity carrier, TruckEntity truck){
        return new AddTruckToCarrier(carrier, truck).execute();
    }

    public boolean removeTruck(CarrierEntity carrier, String truckPlates){
        return new RemoveTruckFromCarrier(carrier, truckPlates).execute();
    }

    public boolean addTautliner(CarrierEntity carrier, TautlinerEntity tautliner){
        return new AddTautlinerToCarrier(carrier, tautliner).execute();
    }

    public boolean removeTautliner(CarrierEntity carrier, String tautlinerPlates){
        return new RemoveTautlinerFromCarrier(carrier, tautlinerPlates).execute();
    }
}
