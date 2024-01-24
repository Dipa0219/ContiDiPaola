package it.polimi.SE2.CK.servlet;

import it.polimi.SE2.CK.DAO.BattleDAO;
import it.polimi.SE2.CK.DAO.TournamentDAO;
import it.polimi.SE2.CK.DAO.UserDAO;
import it.polimi.SE2.CK.bean.Battle;
import it.polimi.SE2.CK.bean.SessionUser;
import it.polimi.SE2.CK.bean.Tournament;
import it.polimi.SE2.CK.utils.EmailManager;
import it.polimi.SE2.CK.utils.FolderManager;
import it.polimi.SE2.CK.utils.ZipFolderManager;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;
import it.polimi.SE2.CK.utils.enumeration.UserRole;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servlet that manage the creation of a tournament.
 */
@WebServlet("/CreateBattle")
@MultipartConfig
public class CreateBattle extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * A connection (session) with a specific database.
     */
    private Connection connection = null;


    /**
     * A convenience method which can be overridden so that there's no need to call super.init(config).
     *
     * @throws ServletException if an exception occurs that interrupts the servlet's normal operation
     */
    public void init() throws ServletException {
        try {
            ServletContext context=getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }
    }

    /**
     * Called by the server (via the service method) to allow a servlet to handle a GET request.
     *
     * @param request object that contains the request the client has made of the servlet
     * @param response object that contains the response the client has made of the servlet
     * @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.getWriter().println("Request non acceptable");
    }

    /**
     * Called by the server (via the service method) to allow a servlet to handle a POST request.
     *
     * @param request object that contains the request the client has made of the servlet
     * @param response object that contains the response the client has made of the servlet
     * @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        //the user is authorized or not - 401 error
        if(session.isNew() || session.getAttribute("user")==null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("You can't access to this page");
            return;
        }

        String battleName = request.getParameter("battleNameInput");
        String battleDescription = request.getParameter("battleDescriptionInput");
        String registrationDeadline = request.getParameter("battleRegistrationDeadlineInput");
        String submissionDeadline = request.getParameter("battleSubmissionDeadlineInput");
        int tournamentId = Integer.parseInt(request.getParameter("TournamentId"));
        Part battleProject = null;
        //500 error
        try {
            battleProject = request.getPart("battleProject");
        }
        catch (ServletException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("All fields with an asterisk are required");
            return;
        }
        Part battleTestCase = null;
        //500 error
        try {
            battleTestCase = request.getPart("battleTestCase");
        }
        catch (ServletException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("All fields with an asterisk are required");
            return;
        }

        //400 error
        if (StringUtils.isAnyEmpty(battleName, registrationDeadline)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("All fields with an asterisk are required");
            return;
        }
        if (FolderManager.getFileName(battleProject)==null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("All fields with an asterisk are required");
            return;
        }
        if (battleName.length()>45){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("The max length of battle name is 45 character");
            return;
        }
        if (battleDescription.length()>200){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("The max length of battle description is 200 character");
            return;
        }

        //transform string in date
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date parseDateRegistrationDeadline;
        Date parseDateSubmissionDeadline;
        //400 error
        try{
            parseDateRegistrationDeadline = dateTimeFormatter.parse(registrationDeadline+":00");
            parseDateSubmissionDeadline = dateTimeFormatter.parse(submissionDeadline+":00");
        }
        catch (ParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Insert a valid data");
            return;
        }
        Timestamp battleRegistrationDeadline = new Timestamp(parseDateRegistrationDeadline.getTime());
        Timestamp battleSubmissionDeadline = new Timestamp(parseDateSubmissionDeadline.getTime());

        //get the actual date
        Date currentDate = new Date();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        //400 error
        if (battleRegistrationDeadline.before(currentTimestamp)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Insert a valid data");
            return;
        }
        if (battleSubmissionDeadline.before(currentTimestamp)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Insert a valid data");
            return;
        }
        if (battleSubmissionDeadline.before(battleRegistrationDeadline)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Insert a valid data");
            return;
        }

        BattleDAO battleDAO = new BattleDAO(connection);

        //there is a battle with the same name
        //500 error
        try {
            //409 error
            if (!battleDAO.checkBattleByName(battleName)){
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println("Existing tournament name, choose another one");
                return;
            }
        }
        catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        //battle project is a .zip file
        //400 error
        if (!Objects.equals(FolderManager.getFileExtension(battleProject), "zip")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Insert a valid project");
            return;
        }

        //battle test case is a yaml file
        //400 error
        if (battleTestCase!=null){
            if (Objects.equals(FolderManager.getFileExtension(battleTestCase), "yaml") ||
                Objects.equals(FolderManager.getFileExtension(battleTestCase), "yml")){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Insert a valid test case");
                return;
            }
        }

        //get session user
        SessionUser user = (SessionUser) session.getAttribute("user");
        TournamentDAO tournamentDAO = new TournamentDAO(connection);
        Tournament tournament = null;
        //500 error
        try {
            tournament = tournamentDAO.showTournamentById(tournamentId);
        }
        catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        //tournament in Ongoing phase
        //406 error
        if (!tournament.getPhase().equals(TournamentState.ONGOING.getValue())){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().println("The tournament has not stared yet");
            return;
        }

        //user is an educator
        //401 error
        if (user.getRole() != UserRole.EDUCATOR.getValue()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("You can't access to this page");
            return;
        }
        //user is in the tournament
        //500 error
        try {
            //401 error
            if (!tournamentDAO.checkUserInTournament(tournament.getId(), user.getId())){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("You can't access to this page");
                return;
            }
        }
        catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        Battle battle = new Battle();
        battle.setName(battleName);
        battle.setDescription(battleDescription);
        battle.setRegDeadline(battleRegistrationDeadline);
        battle.setSubDeadline(battleSubmissionDeadline);

        //save the zip file on disk
        FolderManager.saveFile(battleProject);

        //unzip the zip file
        ZipFolderManager.unzip(FolderManager.getFileName(battleProject));


        /*
            TODO
                creare una repo su GH
                caricare il file zip estratto
                caricare il file yaml se presente
                salvare url repo
                salvare ad DB la battaglia
         */










        //200 ok
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");




//
//        //get session user
//        SessionUser user = (SessionUser) session.getAttribute("user");
//
//        //sets the new tournament data
//        Tournament tournament=new Tournament();
//        tournament.setCreatorId(user.getId());
//        tournament.setCreatorUsername(user.getUsername());
//        tournament.setName(tournamentName);
//        tournament.setDescription(tournamentDescription);
//        tournament.setRegDeadline(battleRegistrationDeadline);
//
//        //creation tournament on DB
//        boolean result;
//        //500 error
//        try {
//            result = tournamentDAO.createTournament(tournament);
//        }
//        catch (SQLException e){
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().println("The server do not respond");
//            return;
//        }
//
//        //500 error
//        if (!result){
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().println("The server do not respond");
//            return;
//        }
//
//        //200 ok
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        //send email to all student
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.submit(() -> sendEmailToAllStudent(tournament.getCreatorUsername(), tournament.getRegDeadline()));
//        executor.shutdownNow();
    }

    /**
     * Email all students enrolled on CKB.
     *
     * @param tournamentCreator the name of the educator that create the tournament.
     * @param time the registration deadline.
     */
    private void sendEmailToAllStudent(String tournamentCreator, Timestamp time){
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
}
