package com.piotrdomagalski.planning.logs;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class NewEntityLog extends LogEntity {

    public NewEntityLog() {
    }

    private NewEntityLog(String uniqueIdentifier, String message, LocalDateTime time) {
        super(uniqueIdentifier, message, time);
    }

    @Override
    public void withUniqueIdentity(String uniqueIdentity) {
        this.uniqueIdentifier = uniqueIdentity;
        this.time = LocalDateTime.now();
    }

    @Override
    public LogEntity withMessage(String username, String relatedTo) {
        this.message = String.format("%s was created by %s",
                this.uniqueIdentifier, username);
        return new NewEntityLog(uniqueIdentifier, message, time);
    }
}

