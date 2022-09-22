package com.piotrdomagalski.planning.tautliner;


public class TautlinerDTOdataParser {

   public static String platesParser(String input) {
        input = input.replaceAll(" ", "");
        return input.toUpperCase();
    }

}
