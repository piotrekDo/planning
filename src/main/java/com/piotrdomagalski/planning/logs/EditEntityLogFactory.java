package com.piotrdomagalski.planning.logs;

public class EditEntityLogFactory extends LogFactory {
    @Override
    LogEntity createLog() {
        return new EditEntityLog();
    }
}
