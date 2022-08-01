package com.piotrdomagalski.planning.tautliner;

/**
 * Utility class allowing to remove all spaces from string and transforming to upper case for data cohesion.
 */

public class TautlinerDTOdataParser {

   public static String platesParser(String input) {
        input = input.replaceAll(" ", "");
        return input.toUpperCase();
    }

}
