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
                       <p> Your token required to change password : %s </p>
                       <p> Token is valid for the next 30 minutes. </p>
                """, token);
    }
}
