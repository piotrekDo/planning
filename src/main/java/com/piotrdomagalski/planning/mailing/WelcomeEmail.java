package com.piotrdomagalski.planning.mailing;

public class WelcomeEmail extends EmailTemplate {
    private final String password;

    public WelcomeEmail(String password) {
        this.password = password;
    }

    @Override
    String setMessage() {
        return String.format("""
                        <p>Twoje nowe hasło to: <span style="color:green;font-size:1.5rem">%s</span></p>
                        <p>Zmień je niezwłocznie po otrzymaniu tej wiadomości </p>
                """, password);
    }
}
