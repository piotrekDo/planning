package com.piotrdomagalski.planning.logs;

public class CoupleTruckTautlinerLogFactory extends LogFactory {
    @Override
    LogEntity createLog() {
        return new CoupleTruckTautlinerLog();
    }
}
