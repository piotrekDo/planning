package com.piotrdomagalski.planning.logs;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class CoupleTruckTautlinerLog extends LogEntity {

    public CoupleTruckTautlinerLog() {
    }

    private CoupleTruckTautlinerLog(String uniqueIdentifier, String message, LocalDateTime time) {
        super(uniqueIdentifier, message, time);
    }

    @Override
    public LogEntity withMessage(String username, String relatedTo) {
        this.message = relatedTo != null ?
                String.format("%s was coupled with %s by %s",
                        this.uniqueIdentifier, relatedTo, username) :
                String.format("%s was uncoupled by %s",
                        this.uniqueIdentifier, username);
        return new CoupleTruckTautlinerLog(uniqueIdentifier, message, time);
    }
}
