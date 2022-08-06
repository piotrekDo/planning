package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.TautlinerTransformer;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckTransformer;
import com.piotrdomagalski.planning.truck_driver.TruckDriverTransformer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrierTransformer {

    private final TruckTransformer truckTransformer;
    private final TruckDriverTransformer driverTransformer;
    private final TautlinerTransformer tautlinerTransformer;

    public CarrierTransformer(TruckTransformer truckTransformer, TruckDriverTransformer driverTransformer, TautlinerTransformer tautlinerTransformer) {
        this.truckTransformer = truckTransformer;
        this.driverTransformer = driverTransformer;
        this.tautlinerTransformer = tautlinerTransformer;
    }

    CarrierFullIDto toCarrierFullIDto(CarrierEntity carrier) {
        return new CarrierFullIDto(
                carrier.getSap(),
                carrier.getName(),
                carrier.getOrigin(),
                carrier.getRate(),
                carrier.getTrucks().stream().map(truckTransformer::toinfoDto).collect(Collectors.toList()),
                carrier.getDrivers().stream().map(driverTransformer::entityToDriverInfoDto).collect(Collectors.toList()),
                carrier.getTautliners().stream().map(tautlinerTransformer::entityToInfoDTO).collect(Collectors.toList())
        );
    }

    CarrierTrucksDTO toCarrierTrucksDto(CarrierEntity carrier) {
        return new CarrierTrucksDTO(
                carrier.getSap(),
                carrier.getName(),
                carrier.getOrigin(),
                carrier.getRate(),
                carrier.getTrucks().stream()
                        .map(truckTransformer::toinfoDto)
                        .collect(Collectors.toList())
        );
    }


    CarrierEntity newUpdateToEntity(CarrierNewUpdateDTO carrier) {
        return CarrierEntity.newCarrier(
                carrier.getSap(),
                carrier.getName(),
                carrier.getName(),
                carrier.getRate()
        );
    }

    CarrierNewUpdateDTO entityToNewUpdateDto(CarrierEntity carrier) {
        return new CarrierNewUpdateDTO(
                carrier.getSap(),
                carrier.getName(),
                carrier.getOrigin(),
                carrier.getRate()
        );
    }

    CarrierShortInfoDTO entityToShortInfoDto(CarrierEntity carrier) {
        return new CarrierShortInfoDTO(
                carrier.getSap(),
                carrier.getName(),
                carrier.getOrigin(),
                carrier.getRate(),
                carrier.getTrucks().size(),
                getMegas(carrier.getTrucks())
        );
    }

    private int getMegas(List<TruckEntity> trucks) {
        long megas = trucks.stream()
                .filter(TruckEntity::getMega)
                .count();
        return (int) megas;
    }

}
