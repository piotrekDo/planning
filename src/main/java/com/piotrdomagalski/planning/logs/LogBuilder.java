package com.piotrdomagalski.planning.logs;

interface LogBuilder {


    void withUniqueIdentity(String uniqueIdentity);

    LogEntity withMessage(String username, String relatedTo);
}
