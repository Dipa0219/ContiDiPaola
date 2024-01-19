package it.polimi.SE2.CK.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

/**
 * Class that send the email using gmail API.
 */
public class EmailManager {
    /**
     * CKB email.
     */
    private static final String email ="code.kata.battle.ckb@gmail.com";
    /**
     * CKB password.
     */
    private static final String password="matn oohm xsuz onvz"; //app password - google --> 2-factor authentication


    /**
     * Sends an email to a specific email.
     *
     * @param to the receiver.
     * @param subject the email subject.
     * @param text the email text.
     * @throws MessagingException This exception is thrown when the connect method on a Store or Transport object fails due to an authentication failure.
     */
    public static void sendEmail(String to, String subject, String text) throws MessagingException {
        //sets the emailAPI property
        Properties properties=new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        //sets the authentication data
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        //sends the email
        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(email));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}