package com.piotrdomagalski.planning.mailing;

/**
 * E-mail message template for sending password-reset tokens to users.
 */

public class RequestPasswordResetEmail extends EmailTemplate {

    private final String token;

    public RequestPasswordResetEmail(String token) {
        this.token = token;
    }

    @Override
    String setMessage() {
        return String.format("""
                       <p> Twój kod do zmiany hasła to : %s </p>
                       <p> Kod jest ważny 30 minut od wygenerowania. </p>
                """, token);
    }
}
