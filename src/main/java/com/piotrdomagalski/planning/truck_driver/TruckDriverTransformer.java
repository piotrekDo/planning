package com.piotrdomagalski.planning.truck_driver;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.springframework.stereotype.Service;

import static com.piotrdomagalski.planning.truck_driver.TruckDriverDTOdataParser.*;

/**
 * Transformer class for drivers allowing to transform DTO to entity and entity to DTO.
 * Method transforming to entity using parsers to ensure data cohesion.
 */

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

    String transformToPreviousNameAndId(String nameChanged, String idDocumentChanged, String originalName, String originalId) {
        if (nameChanged != null && idDocumentChanged == null)
            return nameChanged + " " + originalId;
        else if (nameChanged == null && idDocumentChanged != null)
            return originalName + " " + idDocumentChanged;
        else if (nameChanged != null && idDocumentChanged != null)
            return nameChanged + " " + idDocumentChanged;
        else return null;
    }

}
