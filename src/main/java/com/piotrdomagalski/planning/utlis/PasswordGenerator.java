package com.piotrdomagalski.planning.utlis;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;

public class PasswordGenerator {

    public static String generatePassword() {
        CharacterRule LCR = new CharacterRule(EnglishCharacterData.LowerCase);
        LCR.setNumberOfCharacters(2);
        CharacterRule UCR = new CharacterRule(EnglishCharacterData.UpperCase);
        UCR.setNumberOfCharacters(2);
        CharacterRule DR = new CharacterRule(EnglishCharacterData.Digit);
        DR.setNumberOfCharacters(4);
        org.passay.PasswordGenerator passGen = new org.passay.PasswordGenerator();
        return passGen.generatePassword(8, LCR, UCR, DR);
    }
}
