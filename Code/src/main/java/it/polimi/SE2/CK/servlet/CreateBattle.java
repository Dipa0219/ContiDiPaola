package it.polimi.SE2.CK.servlet;

import it.polimi.SE2.CK.DAO.BattleDAO;
import it.polimi.SE2.CK.DAO.TournamentDAO;
import it.polimi.SE2.CK.bean.Battle;
import it.polimi.SE2.CK.bean.SessionUser;
import it.polimi.SE2.CK.bean.Tournament;
import it.polimi.SE2.CK.utils.EmailManager;
import it.polimi.SE2.CK.utils.GitHubManager;
import it.polimi.SE2.CK.utils.folder.FolderManager;
import it.polimi.SE2.CK.utils.folder.ZipFolderManager;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;
import it.polimi.SE2.CK.utils.enumeration.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        response.setHeader("X-Frame-Options", "DENY"); //do not allow the page to be included in any frame or iframe
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains"); //your application should only be accessible via a secure connection (HTTPS)
        response.setHeader("Content-Security-Policy", "default-src 'self'"); //resources must come from the same source
        response.setHeader("X-Content-Type-Options", "nosniff"); //prevents browsers from interpreting files as anything other than their declared MIME type
        response.setHeader("X-XSS-Protection", "1; mode=block"); //block the page if an XSS attack is detected

        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.getWriter().println("Request non acceptable");

        String path = "ErrorPage.html";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called by the server (via the service method) to allow a servlet to handle a POST request.
     *
     * @param request object that contains the request the client has made of the servlet
     * @param response object that contains the response the client has made of the servlet
     * @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("X-Frame-Options", "DENY"); //do not allow the page to be included in any frame or iframe
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains"); //your application should only be accessible via a secure connection (HTTPS)
        response.setHeader("Content-Security-Policy", "default-src 'self'"); //resources must come from the same source
        response.setHeader("X-Content-Type-Options", "nosniff"); //prevents browsers from interpreting files as anything other than their declared MIME type
        response.setHeader("X-XSS-Protection", "1; mode=block"); //block the page if an XSS attack is detected

        HttpSession session = request.getSession();
        //the user is authorized or not - 401 error
        if(session.isNew() || session.getAttribute("user")==null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("You can't access to this page");
            return;
        }

        String battleName = StringEscapeUtils.escapeHtml4(request.getParameter("battleNameInput"));
        String battleDescription = StringEscapeUtils.escapeHtml4(request.getParameter("battleDescriptionInput"));
        String registrationDeadline = StringEscapeUtils.escapeHtml4(request.getParameter("battleRegistrationDeadlineInput"));
        String submissionDeadline = StringEscapeUtils.escapeHtml4(request.getParameter("battleSubmissionDeadlineInput"));
        int tournamentId;
        try {
             tournamentId = Integer.parseInt(StringEscapeUtils.escapeHtml4(request.getParameter("TournamentId")));
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Internal error with the page, please try again");
            return;
        }

        if (tournamentId<=0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Internal error with the page, please try again");
            return;
        }

        int minStudentPerTeam;
        try {
            minStudentPerTeam = Integer.parseInt(StringEscapeUtils.escapeHtml4(request.getParameter("minStudentPerTeamInput")));
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("You must insert a number in minimum number of student");
            return;
        }

        int maxStudentPerTeam;
        try {
            maxStudentPerTeam = Integer.parseInt(StringEscapeUtils.escapeHtml4(request.getParameter("maxStudentPerTeamInput")));
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("You must insert a number in maximum number of student");
            return;
        }

        //number student per team
        //400 error
        if (minStudentPerTeam < 1 || maxStudentPerTeam < 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Minimum and maximum number of student bust be greater than 0");
            return;
        }

        //400 error
        if (minStudentPerTeam > maxStudentPerTeam) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Minimum number of student can't be greater than maximum");
            return;
        }

        Part battleProject = null;
        //400 error
        try {
            battleProject = request.getPart("battleProject");
        }
        catch (ServletException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("All fields with an asterisk are required");
            return;
        }

        Part battleTestCase = null;
        //400 error
        try {
            battleTestCase = request.getPart("battleTestCase");
        }
        catch (ServletException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("All fields with an asterisk are required");
            return;
        }

        //400 error
        if (StringUtils.isAnyEmpty(battleName, registrationDeadline, submissionDeadline)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("All fields with an asterisk are required");
            return;
        }

        //400 error
        if (FolderManager.getFileName(battleProject)==null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("All fields with an asterisk are required");
            return;
        }

        //400 error
        if (battleName.length()>45){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("The max length of battle name is 45 character");
            return;
        }
        //400 error
        if (battleDescription!=null) {
            if (battleDescription.length() > 200) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("The max length of battle description is 200 character");
                return;
            }
        }

        //valid date
        //400 error
        if (registrationDeadline.length()>16){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("You must insert a valid date");
            return;
        }
        //400 error
        if (submissionDeadline.length()>16){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("You must insert a valid date");
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
            response.getWriter().println("Insert a valid date");
            return;
        }
        Timestamp battleRegistrationDeadline = new Timestamp(parseDateRegistrationDeadline.getTime());
        Timestamp battleSubmissionDeadline = new Timestamp(parseDateSubmissionDeadline.getTime());

        //get the actual date
        Date currentDate = new Date();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        //date error
        //400 error
        if (battleRegistrationDeadline.before(currentTimestamp)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Registration deadline must be after now");
            return;
        }
        //400 error
        if (battleSubmissionDeadline.before(currentTimestamp)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Submission deadline must be after now");
            return;
        }
        //400 error
        if (battleSubmissionDeadline.before(battleRegistrationDeadline)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Registration deadline must be before submission deadline");
            return;
        }

        BattleDAO battleDAO = new BattleDAO(connection);

        //there is a battle with the same name
        //500 error
        try {
            //409 error
            if (battleDAO.checkBattleByName(battleName)){
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println("Existing battle name, choose another one");
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
        if (battleTestCase.getSize() > 0){
            if (!Objects.equals(FolderManager.getFileExtension(battleTestCase), "yaml") &&
                !Objects.equals(FolderManager.getFileExtension(battleTestCase), "yml")){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Insert a valid test case");
                return;
            }
        }

        //get session user
        SessionUser user = (SessionUser) session.getAttribute("user");
        TournamentDAO tournamentDAO = new TournamentDAO(connection);
        Tournament tournament;
        //500 error
        try {
            tournament = tournamentDAO.showTournamentById(tournamentId);
        }
        catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        if (tournament==null){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The chosen tournament doesn't exist");
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
                response.getWriter().println("Only collaborator can create battles");
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
        battle.setMinNumStudent(minStudentPerTeam);
        battle.setMaxNumStudent(maxStudentPerTeam);
        battle.setTournamentId(tournamentId);
        battle.setTournamentName(tournament.getName());
        battle.setPhase(TournamentState.NOTSTARTED);

        //save the zip file on disk
        FolderManager.saveFile(battleProject);

        //unzip the zip file
        ZipFolderManager.unzip(FolderManager.getFileName(battleProject));

        //save the yaml file on disk
        if (battleTestCase.getSize() > 0){
            FolderManager.saveFile(battleTestCase,
                    FolderManager.getDirectory() + FolderManager.getFileName(battleProject) +
                            FolderManager.getPath() + ".github" + FolderManager.getPath() + "workflows");
        }

        //creation of GitHub repository
        GitHubManager.createGitHubRepository(battle.getName(), true);

        //upload project file on GitHub repository
        GitHubManager.uploadFolderOnGitHubRepository(FolderManager.getDirectory() + FolderManager.getFileName(battleProject),
                GitHubManager.getRepoURL() + battle.getName());

        //set the GitHub repository where the battle is saved
        battle.setGitHubBattleRepository(GitHubManager.getRepoURL() + battle.getName());

        //creation battle on DB
        boolean result;
        //500 error
        try {
            result = battleDAO.createBattle(battle);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        //500 error
        if (!result){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        //200 ok
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //delete the zip file
        ZipFolderManager.deleteZipFile(FolderManager.getFileName(battleProject) + "." + FolderManager.getFileExtension(battleProject));
        //delete the GitHub repository directory
        FolderManager.deleteDirectory(new File(FolderManager.getDirectory() + FolderManager.getFileName(battleProject)));

        //send email to all student in a tournament
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Tournament finalTournament = tournament;
        executor.submit(() ->
                EmailManager.sendEmailToAllStudentEnrolledInTournamentCreationBattle(battle, connection));
        executor.shutdownNow();
    }
}
