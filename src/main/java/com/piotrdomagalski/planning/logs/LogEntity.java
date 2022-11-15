package com.piotrdomagalski.planning.logs;


import com.piotrdomagalski.planning.app.DatabaseEntity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class LogEntity extends DatabaseEntity implements LogBuilder{
    protected String uniqueIdentifier;
    protected String message;
    protected LocalDateTime time;

    public LogEntity() {
    }

    public LogEntity(String uniqueIdentifier, String message, LocalDateTime time) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.message = message;
        this.time = time;
    }

    public LogEntity(Long id, String uniqueIdentifier, String message, LocalDateTime time) {
        super(id);
        this.uniqueIdentifier = uniqueIdentifier;
        this.message = message;
        this.time = time;
    }

    public void withUniqueIdentity(String uniqueIdentity) {
        this.uniqueIdentifier = uniqueIdentity;
        this.time = LocalDateTime.now();
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
