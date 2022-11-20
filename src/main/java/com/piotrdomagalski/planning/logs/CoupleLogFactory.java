package com.piotrdomagalski.planning.logs;

public class CoupleLogFactory extends LogFactory {
    @Override
    LogEntity createLog() {
        return new CoupleLog();
    }
}
