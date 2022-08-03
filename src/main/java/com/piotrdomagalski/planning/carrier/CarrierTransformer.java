package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.truck.TruckEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrierTransformer {

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
