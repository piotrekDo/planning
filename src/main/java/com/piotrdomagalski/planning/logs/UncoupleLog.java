package com.piotrdomagalski.planning.logs;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class UncoupleLog extends LogEntity {
    public UncoupleLog() {
    }

    private UncoupleLog(String uniqueIdentifier, String message, LocalDateTime time) {
        super(uniqueIdentifier, message, time);
    }

    @Override
    public LogEntity withMessage(String username, String relatedTo) {
        this.message = String.format("%s was uncoupled by %s",
                relatedTo, username);
        return new UncoupleLog(uniqueIdentifier, message, time);
    }
}
