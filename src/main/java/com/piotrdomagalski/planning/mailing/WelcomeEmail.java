package com.piotrdomagalski.planning.mailing;

/**
 * E-mail message template used to greet newly registered users.
 */

public class WelcomeEmail extends EmailTemplate {
    private final String password;

    public WelcomeEmail(String password) {
        this.password = password;
    }

    @Override
    String setMessage() {
        return String.format("""
                        <p>Your new password: <span style="color:green;font-size:1.5rem">%s</span></p>
                        <p>Please change it immediately after receiving this message </p>
                """, password);
    }
}
