package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.springframework.stereotype.Service;

@Service
public class TruckTransformer {

    public TruckInfoDTO toinfoDto(TruckEntity entity) {
        CarrierEntity carrier = entity.getCarrier();
        TautlinerEntity tautliner = entity.getTautliner();
        TruckDriverEntity truckDriver = entity.getTruckDriver();
        return new TruckInfoDTO(
                entity.getTruckPlates(),
                entity.getMega(),
                carrier != null ? carrier.getSap() : null,
                carrier != null ? carrier.getName() : null,
                truckDriver != null ? truckDriver.getId() : null,
                truckDriver != null ? truckDriver.getFullName() : null,
                truckDriver != null ? truckDriver.getTel() : null,
                truckDriver != null ? truckDriver.getIdDocument() : null,
                tautliner != null ? tautliner.getTautlinerPlates() : null,
                tautliner != null ? tautliner.getTechInspection() : null,
                tautliner != null ? tautliner.getXpo() : null
        );
    }

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
