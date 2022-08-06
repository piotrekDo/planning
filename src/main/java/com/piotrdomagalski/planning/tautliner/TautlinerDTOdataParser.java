package com.piotrdomagalski.planning.tautliner;


public class TautlinerDTOdataParser {

    static String platesParser(String input) {
        input = input.replaceAll(" ", "");
        return input.toUpperCase();
    }

}
