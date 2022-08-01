package com.piotrdomagalski.planning.utlis;

public class StringUtlis {

    public static String capitalize(String string) {
        StringBuilder result = new StringBuilder();
        string = string.replaceAll("( )+", " ");
        String[] split = string.split(" ");
        for (int i = 0; i < split.length; i++) {
            result.append(split[i].substring(0, 1).toUpperCase()).append(split[i].substring(1).toLowerCase());
            if (i +1 < split.length && split[i + 1] != null) {
                result.append(" ");
            }
        }
        return result.toString();
    }

}
