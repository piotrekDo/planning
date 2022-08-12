package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import static com.piotrdomagalski.planning.tautliner.TautlinerDTOdataParser.*;

@Service
public class TautlinerTransformer {

    DateTimeFormatter formatter =
            new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();

    public TautlinerInfoDTO entityToInfoDTO(TautlinerEntity entity) {
        CarrierEntity carrier = entity.getCarrier();
        TruckEntity truck = entity.getTruck();
        return new TautlinerInfoDTO(
                entity.getTautlinerPlates(),
                entity.getTechInspection(),
                entity.getXpo(),
                carrier != null ? carrier.getName() : null,
                carrier != null ? carrier.getSap() : null,
                truck != null ? truck.getTruckPlates() : null
        );
    }

    TautlinerEntity newUpdateDTOtoEntity(TautlinerNewUpdateDTO dto) {
        return TautlinerEntity.newTautliner(
                dto.getXpo(),
                dto.getTautlinerPlates() != null? platesParser(dto.getTautlinerPlates()) : null,
                dto.getTechInspection() != null ? LocalDateTime.parse(dto.getTechInspection(), formatter) : null
        );
    }

    TautlinerNewUpdateDTO entityToNewUpdateDTO(TautlinerEntity entity) {
        return new TautlinerNewUpdateDTO(
                entity.getXpo(),
                entity.getTautlinerPlates(),
                entity.getTechInspection().toString()
        );
    }

}
