package com.piotrdomagalski.planning.truck_driver;

import org.springframework.stereotype.Service;

@Service
public class TruckDriverTransformer {

    TruckDriverEntity newUpdatDriverDtoToEntity(TruckDriverNewUpdateDTO dto) {
        return TruckDriverEntity.newTruckDriver(
                dto.getFullName(),
                dto.getTel(),
                dto.getIdDocument()
        );
    }

    TruckDriverNewUpdateDTO entityToNewUpdateDriverDto(TruckDriverEntity entity) {
        return new TruckDriverNewUpdateDTO(
                entity.getFullName(),
                entity.getTel(),
                entity.getIdDocument()
        );
    }

}
