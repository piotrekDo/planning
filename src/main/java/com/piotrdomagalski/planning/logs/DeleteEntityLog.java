package com.piotrdomagalski.planning.logs;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class DeleteEntityLog extends LogEntity {

    public DeleteEntityLog() {
    }

    private DeleteEntityLog(String uniqueIdentifier, String message, LocalDateTime time) {
        super(uniqueIdentifier, message, time);
    }

    @Override
    public LogEntity withMessage(String username, String relatedTo) {
        this.message = String.format("%s was deleted by %s",
                this.uniqueIdentifier, username);
        return new DeleteEntityLog(uniqueIdentifier, message, time);
    }
}
