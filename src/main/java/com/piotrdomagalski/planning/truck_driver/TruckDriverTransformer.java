package com.piotrdomagalski.planning.truck_driver;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.springframework.stereotype.Service;

import static com.piotrdomagalski.planning.truck_driver.TruckDriverDTOdataParser.*;


@Service
public class TruckDriverTransformer {

    public TruckDriverInfoDTO entityToDriverInfoDto(TruckDriverEntity entity) {
        CarrierEntity carrier = entity.getCarrier();
        TruckEntity truck = entity.getTruck();
        return new TruckDriverInfoDTO(
                entity.getId(),
                entity.getFullName(),
                entity.getTel(),
                entity.getIdDocument(),
                carrier != null ? carrier.getName() : null,
                carrier != null ? carrier.getSap() : null,
                truck != null ? truck.getTruckPlates() : null
        );
    }

    TruckDriverEntity newUpdatDriverDtoToEntity(TruckDriverNewUpdateDTO dto) {
        return TruckDriverEntity.newTruckDriver(
                dto.getFullName() != null ? fullNameParser(dto.getFullName()) : null,
                dto.getTel() != null ? telParser(dto.getTel()) : null,
                dto.getIdDocument() != null ? idDocumentParser(dto.getIdDocument()) : null
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
