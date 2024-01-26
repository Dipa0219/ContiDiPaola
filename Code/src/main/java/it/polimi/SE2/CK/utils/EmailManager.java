package it.polimi.SE2.CK.utils;

import it.polimi.SE2.CK.DAO.UserDAO;
import it.polimi.SE2.CK.bean.Battle;
import it.polimi.SE2.CK.bean.Tournament;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

/**
 * Class that send the email using gmail API.
 */
public class EmailManager {
    /**
     * CKB email.
     */
    private static final String email = "code.kata.battle.ckb@gmail.com";
    /**
     * CKB password.
     */
    private static final String password = "matn oohm xsuz onvz"; //app password - google --> 2-factor authentication


    /**
     * Sends an email to a specific email.
     *
     * @param to the receiver.
     * @param subject the email subject.
     * @param text the email text.
     * @throws MessagingException This exception is thrown when the connect method on a Store or Transport object fails due to an authentication failure.
     */
    private static void sendEmail(String to, String subject, String text) throws MessagingException {
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


    /**
     * Email all students enrolled on CKB that a new tournament has been created.
     *
     * @param tournamentCreator the name of the educator that create the tournament.
     * @param time the registration deadline.
     * @param connection the connection (session) with a specific database.
     */
    public static void sendEmailToAllStudentNewTournamentCreated(String tournamentCreator, Timestamp time, Connection connection){
        UserDAO userDAO=new UserDAO(connection);
        List<String> emailAccount= null;
        try {
            emailAccount = userDAO.allStudentEmail();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String object="A new tournament has been created";
        String text=tournamentCreator + " created a new tournament. \n" +
                "Hurry up, you only have until " + time +
                "\nIf you are interested log on to the CKB platform now";

        for (String s : emailAccount) {
            try {
                EmailManager.sendEmail(s, object, text);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

        }

    }

    /**
     * Email all students enrolled on CKB that a tournament has been closed.
     *
     * @param tournament the interested tournament.
     * @param connection the connection (session) with a specific database.
     */
    public static void sendEmailToAllStudentEnrolledInTournamentClosed(Tournament tournament, Connection connection){
        UserDAO userDAO=new UserDAO(connection);
        List<String> emailAccount= null;
        try {
            emailAccount = userDAO.allStudentTournamentEmail(tournament.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String object = "A tournament has been closed";
        String text = tournament.getCreatorUsername() + " closed the " + tournament.getName() + " tournament. \n" +
                "The final rankings are available on the tournament page.";

        for (String s : emailAccount) {
            try {
                EmailManager.sendEmail(s, object, text);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * Email all students enrolled on CKB that a new battle in the tournament has been created.
     *
     * @param battle the interested battle.
     * @param connection the connection (session) with a specific database.
     */
    public static void sendEmailToAllStudentEnrolledInTournamentCreationBattle(Battle battle, Connection connection){
        UserDAO userDAO=new UserDAO(connection);
        List<String> emailAccount= null;
        try {
            emailAccount = userDAO.allStudentTournamentEmail(battle.getId()); //TODO
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String object = "A battle has been created";
        String text = battle.getName() + "battle has been created. " +
                "The battle belongs to the " + battle.getTournamentName() + " tournament.\n" +
                "Hurry up, you only have until " + battle.getRegDeadline() +
                "\nIf you are interested log on to the CKB platform now";

        for (String s : emailAccount) {
            try {
                EmailManager.sendEmail(s, object, text);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

        }
    }
}