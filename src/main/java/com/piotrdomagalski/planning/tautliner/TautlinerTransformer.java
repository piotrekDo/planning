package com.piotrdomagalski.planning.tautliner;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Service
public class TautlinerTransformer {

    DateTimeFormatter formatter =
            new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();

    TautlinerEntity newUpdateDTOtoEntity(TautlinerNewUpdateDTO dto) {
        return TautlinerEntity.newTautliner(
                dto.getXpo(),
                dto.getTautlinerPlates(),
                LocalDateTime.parse(dto.getTechInspection(), formatter)
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
