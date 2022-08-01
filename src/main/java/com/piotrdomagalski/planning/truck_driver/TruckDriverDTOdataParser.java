package com.piotrdomagalski.planning.truck_driver;

import com.piotrdomagalski.planning.utlis.StringUtlis;

/**
 * Utility class providing tools to standardize data for data cohesion.
 */

public class TruckDriverDTOdataParser {

    static String fullNameParser(String input) {
        input = input.replaceAll("( )+", " ");
        return StringUtlis.capitalize(input.trim());
    }

    static String telParser(String input) {
        return String.join("-",
                input.substring(0, 3),
                input.substring(3, 6),
                input.substring(6, 9));
    }

    static String idDocumentParser(String input) {
        input = input.replaceAll(" ", "");
        return input.toUpperCase();
    }

}
