package com.piotrdomagalski.planning.logs;

public class DeleteEntityFactory extends LogFactory {
    @Override
    LogEntity createLog() {
        return new DeleteEntityLog();
    }
}
