package com.piotrdomagalski.planning.logs;

import java.time.LocalDateTime;

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
