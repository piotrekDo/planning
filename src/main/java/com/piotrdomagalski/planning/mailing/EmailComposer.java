package com.piotrdomagalski.planning.mailing;

import com.piotrdomagalski.planning.app_user.AppUser;
import com.piotrdomagalski.planning.app_user.AppUserCreateNewDto;
import com.piotrdomagalski.planning.app_user.PasswordChangeRequestDto;
import org.springframework.stereotype.Component;

import static com.piotrdomagalski.planning.mailing.Mailbox.MAILBOX_PASSWORD;
import static com.piotrdomagalski.planning.mailing.Mailbox.MAILBOX_REGISTER;

/**
 * Utility class used for user registration and password management. Creates respective e-mail message.
 * Used in AppUserService. 
 */

@Component
public class EmailComposer {
    public void sendRegistrationMessage(AppUserCreateNewDto appUser, String password) {
        EmailTemplate mail = new WelcomeEmail(password);
        String message = mail.create(appUser.getUsername());

        EmailSender emailSender = new EmailSender(MAILBOX_REGISTER);
        emailSender.sendMail("Planning account registration confirmation",
                message,
                appUser.getUserEmail());
    }

    public void sendResetPasswordToken(PasswordChangeRequestDto request, String token) {
        EmailTemplate mail = new RequestPasswordResetEmail(token);
        String message = mail.create(request.getUsername());

        EmailSender emailSender = new EmailSender(MAILBOX_PASSWORD);
        emailSender.sendMail("Changing your Planning login password",
                message,
                request.getEmail());
    }

    public void sendPasswordChangeConformation(AppUser appUser) {
        EmailTemplate mail = new PasswordChangedEmail();
        String message = mail.create(appUser.getUsername());

        EmailSender emailSender = new EmailSender(MAILBOX_PASSWORD);
        emailSender.sendMail("Changing your Planning login password",
                message,
                appUser.getUserEmail());
    }

}
