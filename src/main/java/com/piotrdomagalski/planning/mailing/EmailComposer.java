package com.piotrdomagalski.planning.mailing;

import com.piotrdomagalski.planning.app_user.AppUser;
import com.piotrdomagalski.planning.app_user.AppUserCreateNewDto;
import com.piotrdomagalski.planning.app_user.PasswordChangeRequestDto;
import org.springframework.stereotype.Component;

import static com.piotrdomagalski.planning.mailing.Mailbox.MAILBOX_PASSWORD;
import static com.piotrdomagalski.planning.mailing.Mailbox.MAILBOX_REGISTER;

@Component
public class EmailComposer {
    public void sendRegistrationMessage(AppUserCreateNewDto appUser, String password) {
        EmailTemplate mail = new WelcomeEmail(password);
        String message = mail.create(appUser.getUsername());

        EmailSender emailSender = new EmailSender(MAILBOX_REGISTER);
        emailSender.sendMail("Potwierdzenie rejestracji konta w serwisie Planning",
                message,
                appUser.getUserEmail());
    }

    public void sendResetPasswordToken(PasswordChangeRequestDto request, String token) {
        EmailTemplate mail = new RequestPasswordResetEmail(token);
        String message = mail.create(request.getUsername());

        EmailSender emailSender = new EmailSender(MAILBOX_PASSWORD);
        emailSender.sendMail("Zmiana hasła logowania w serwisie Planning",
                message,
                request.getEmail());
    }

    public void sendPasswordChangeConformation(AppUser appUser) {
        EmailTemplate mail = new PasswordChangedEmail();
        String message = mail.create(appUser.getUsername());

        EmailSender emailSender = new EmailSender(MAILBOX_PASSWORD);
        emailSender.sendMail("Zmiana hasła logowania w serwisie Planning",
                message,
                appUser.getUserEmail());
    }

}
