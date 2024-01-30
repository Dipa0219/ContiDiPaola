package it.polimi.SE2.CK.utils;

import it.polimi.SE2.CK.DAO.BattleDAO;
import it.polimi.SE2.CK.DAO.TournamentDAO;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final String password = "matnoohmxsuzonvz"; //app password - google --> 2-factor authentication

    /**
     * The regular expression pattern used to validate email addresses.
     */
    private static final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * The compiled pattern object used for email address validation.
     */
    private static final Pattern pattern = Pattern.compile(emailPattern);


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
     * Validates an email address.
     *
     * @param email the email to check.
     * @return true if the email is a valid one.
     */
    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
     * Email all students enrolled on specific tournament that it has been closed.
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
     * Email all students enrolled on specific tournament that a new battle has been created.
     *
     * @param battle the interested battle.
     * @param connection the connection (session) with a specific database.
     */
    public static void sendEmailToAllStudentEnrolledInTournamentCreationBattle(Battle battle, Connection connection){
        UserDAO userDAO=new UserDAO(connection);
        List<String> emailAccount= null;
        try {
            emailAccount = userDAO.allStudentTournamentEmail(battle.getId());
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

    /**
     * Email all students enrolled on specific tournament that it has been started.
     *
     * @param tournamentId the tournament id.
     * @param connection the connection (session) with a specific database.
     */
    public static void sendEmailToAllStudentEnrolledInTournamentStarted(int tournamentId, Connection connection) {
        UserDAO userDAO=new UserDAO(connection);
        List<String> emailAccount= null;
        try {
            emailAccount = userDAO.allStudentTournamentEmail(tournamentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TournamentDAO tournamentDAO = new TournamentDAO(connection);
        Tournament tournament = null;
        try {
            tournament = tournamentDAO.showTournamentById(tournamentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String object = "A tournament has been started";
        String text = tournament.getName() + "tournament has been started. ";

        for (String s : emailAccount) {
            try {
                EmailManager.sendEmail(s, object, text);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Email all educator that manage a specific tournament that it has been closed.
     *
     * @param tournamentId the interested tournament.
     * @param connection the connection (session) with a specific database.
     */
    public static void sendEmailToAllCollaboratorInTournamentClosed(int tournamentId, Connection connection){
        UserDAO userDAO=new UserDAO(connection);
        TournamentDAO tournamentDAO = new TournamentDAO(connection);
        Tournament tournament = null;
        try {
            tournament = tournamentDAO.showTournamentById(tournamentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<String> emailAccount= null;
        try {
            emailAccount = userDAO.allEducatorTournamentEmail(tournamentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String object = "A tournament has been closed";
        String text = tournament.getName() + " is close because no student have subscribed.";

        for (String s : emailAccount) {
            try {
                EmailManager.sendEmail(s, object, text);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Email all student enrolled in o specific battle that it has been closed.
     *
     * @param battleId the interested battle.
     * @param connection the connection (session) with a specific database.
     */
    public static void sendEmailToAllStudentBattleClosed(int battleId, Connection connection){
        UserDAO userDAO=new UserDAO(connection);
        BattleDAO battleDAO = new BattleDAO(connection);
        Battle battle = null;
        try {
            battle = battleDAO.showBattleById(battleId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<String> emailAccount= null;
        try {
            emailAccount = userDAO.allStudentBattleEmail(battleId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String object = "A battle has been closed";
        String text = battle.getName() + " is closed. \n" +
                "The final rankings are available on the battle page.";

        for (String s : emailAccount) {
            try {
                EmailManager.sendEmail(s, object, text);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}