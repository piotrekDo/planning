package com.piotrdomagalski.planning.mailing;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import static org.apache.commons.mail.EmailConstants.UTF_8;

public class EmailSender {

    private final Mailbox mailbox;

    public EmailSender(Mailbox mailbox) {
        this.mailbox = mailbox;
    }

    private final String HOST_NAME = "mx1.linux.pl";

    void sendMail(String subject, String msg, String mailTo) {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(HOST_NAME);
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator(mailbox.getAddress(), mailbox.getPass()));
            email.setSSLOnConnect(true);
            email.addTo(mailTo, mailTo);
            email.setFrom(mailbox.getAddress(), "Planning");
            email.setSubject(subject);
            email.setCharset(UTF_8);

            // embed the image and get the content id
//                URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
//                String cid = email.embed(url, "Apache logo");
            email.setHtmlMsg(msg);

            // set the alternative message
            email.setTextMsg("Your email client does not support HTML messages");
            email.send();

        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
