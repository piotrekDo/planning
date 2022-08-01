package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.springframework.stereotype.Service;

@Service
public class CarrierActions {

    public boolean clear(CarrierEntity carrier){
        return new ClearCarrierAction(carrier).execute();
    }

    public boolean addDriver(CarrierEntity carrier, TruckDriverEntity driver){
        return new AddDriverToCarrierAction(carrier, driver).execute();
    }

    public boolean removeDriver(CarrierEntity carrier, Long driverId){
        return new RemoveTruckDriverFromCarrierAction(carrier, driverId).execute();
    }

    public boolean addTruck(CarrierEntity carrier, TruckEntity truck){
        return new AddTruckToCarrierAction(carrier, truck).execute();
    }

    public boolean removeTruck(CarrierEntity carrier, String truckPlates){
        return new RemoveTruckFromCarrier(carrier, truckPlates).execute();
    }

    public boolean addTautliner(CarrierEntity carrier, TautlinerEntity tautliner){
        return new AddTautlinerToCarrierAction(carrier, tautliner).execute();
    }

    public boolean removeTautliner(CarrierEntity carrier, String tautlinerPlates){
        return new RemoveTautlinerFromCarrierAction(carrier, tautlinerPlates).execute();
    }
}
