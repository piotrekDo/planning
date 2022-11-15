package com.piotrdomagalski.planning.logs;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
class EditEntityLog extends LogEntity {

    public EditEntityLog() {
    }

    private EditEntityLog(String uniqueIdentifier, String message, LocalDateTime time) {
        super(uniqueIdentifier, message, time);
    }

    @Override
    public LogEntity withMessage(String username, String relatedTo) {
        this.message = String.format("%s was edited by %s",
                this.uniqueIdentifier, username);
        return new EditEntityLog(uniqueIdentifier, message, time);
    }
}
