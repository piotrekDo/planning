package com.piotrdomagalski.planning.truck;

import org.springframework.stereotype.Service;

@Service
public class TruckTransformer {

    TruckNewUpdateDTO entityToNewUpdateDto(TruckEntity entity) {
        return new TruckNewUpdateDTO(
                entity.getTruckPlates(),
                entity.getMega()
        );
    }

    TruckEntity newUpdateToEntity(TruckNewUpdateDTO dto) {
        return TruckEntity.newTruck(
                dto.getTruckPlates(),
                dto.getMega()
        );
    }
}
