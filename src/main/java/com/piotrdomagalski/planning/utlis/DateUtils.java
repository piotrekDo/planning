package com.piotrdomagalski.planning.utlis;

import java.util.Date;

public class DateUtils {

    public static Date setMinutes(long minutes) {
        return new Date(System.currentTimeMillis() + (minutes * 60 * 1000));
    }

    public static Date setHours(long hours) {
        return new Date(System.currentTimeMillis() + (hours * 60 * 60 * 1000));
    }
}
