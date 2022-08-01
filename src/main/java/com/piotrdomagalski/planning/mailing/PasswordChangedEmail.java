package com.piotrdomagalski.planning.mailing;

/**
 * E-mail message template for changing password.
 */

public class PasswordChangedEmail extends EmailTemplate {
    @Override
    String setMessage() {
        return "Twoje hasło zostało poprawnie zmienione";
    }
}
