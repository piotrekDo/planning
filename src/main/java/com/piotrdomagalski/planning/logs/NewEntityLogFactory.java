package com.piotrdomagalski.planning.logs;

public class NewEntityLogFactory extends LogFactory{

    @Override
    LogEntity createLog() {
        return new NewEntityLog();
    }
}
