package com.piotrdomagalski.planning.logs;

public class UncoupleLogFactory extends LogFactory {
    @Override
    LogEntity createLog() {
        return new UncoupleLog();
    }
}
