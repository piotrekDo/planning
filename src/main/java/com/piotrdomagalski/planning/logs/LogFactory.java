package com.piotrdomagalski.planning.logs;

public abstract class LogFactory {

    public LogEntity makeLog(String uniqueIdentifier, String username, String relatedTo) {
        LogEntity log = createLog();
        log.withUniqueIdentity(uniqueIdentifier);
        log.withMessage(username, relatedTo);
        return log;
    }

    public LogEntity makeLog(String uniqueIdentifier, String username) {
        return makeLog(uniqueIdentifier, username, null);
    }

    abstract LogEntity createLog();

}
