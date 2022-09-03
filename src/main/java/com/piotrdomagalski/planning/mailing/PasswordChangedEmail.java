package com.piotrdomagalski.planning.mailing;

public class PasswordChangedEmail extends EmailTemplate {
    @Override
    String setMessage() {
        return "Twoje hasło zostało poprawnie zmienione";
    }
}
