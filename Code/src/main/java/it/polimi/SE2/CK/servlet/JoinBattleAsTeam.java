package it.polimi.SE2.CK.servlet;

import it.polimi.SE2.CK.DAO.BattleDAO;
import it.polimi.SE2.CK.DAO.TeamDAO;
import it.polimi.SE2.CK.DAO.UserDAO;
import it.polimi.SE2.CK.bean.Battle;
import it.polimi.SE2.CK.bean.SessionUser;
import it.polimi.SE2.CK.utils.enumeration.UserRole;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet that manage the subscription on a battle with a team. It creates a new team.
 */
@WebServlet("/JoinBattleAsTeam")
@MultipartConfig
public class JoinBattleAsTeam extends HttpServlet {
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

        SessionUser user = (SessionUser) session.getAttribute("user");
        int battleId;
        try {
             battleId = Integer.parseInt(StringEscapeUtils.escapeHtml4(request.getParameter("BattleId")));
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Internal error with the page, please try again");
            return;
        }
        String[] teammateList = request.getParameterValues("teamMateInput");
        String teamName = StringEscapeUtils.escapeHtml4(request.getParameter("teamNameInput"));

        //user is a student
        //401 error
        if (user.getRole() != UserRole.STUDENT.getValue()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("You can't access to this page");
            return;
        }

        //battle exists and is in Not Started phase
        BattleDAO battleDAO = new BattleDAO(connection);
        //500 error
        try {
            //406 error
            if (!battleDAO.checkBattleNotStarted(battleId)){
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getWriter().println("The battle has already begun");
                return;
            }
        }
        catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        Battle battle;
        //500 error
        try {
             battle = battleDAO.showBattleById(battleId);
        }
        catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        //check if the student select at least 1 student
        //400 error
        if (teammateList == null || teammateList.length == 0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("You have to choose a teammate");
            return;
        }

        List<String> listItems = Arrays.asList(teammateList);
        List<Integer> teammate = listItems.stream()
                .map(Integer::parseInt)
                .toList();

        //check if the number of teammates selected is permitted for the battle
        if (teammate.size() + 1 < battle.getMinNumStudent() || teammate.size() + 1 > battle.getMaxNumStudent()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("You have to choose a number of teammates between " + battle.getMinNumStudent() + " and " + battle.getMaxNumStudent());
            return;
        }

        UserDAO userDAO = new UserDAO(connection);
        TeamDAO teamDAO = new TeamDAO(connection);
        for (Integer integer : teammate) {
            //check if all teammates are student
            //406 error
            if (user.getRole() != UserRole.STUDENT.getValue()) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getWriter().println("You have to choose a student as teammate");
                return;
            }

            //check if all teammates are not in a team
            //500 error
            try {
                //409 error
                if (teamDAO.checkStudentInOtherTeam(integer, battleId)) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().println("You select a student that is already signed up to another team");
                    return;
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("The server do not respond");
                return;
            }
        }

        //check if user has already creates a team
        //500 error
        try {
            //401 error
            if (teamDAO.checkStudentHasCreatedATeam(user.getId(), battleId)) {
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

        //check if the team name is already in use
        //500 error
        try {
            //409 error
            if (teamDAO.checkTeamName(teamName, battleId)){
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println("The name is already in use for the battle");
                return;
            }
        }
        catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        //join battle as a team
        boolean result;
        try {
            result = teamDAO.joinBattleAsTeam(user.getId(), battleId, teammate, teamName);
        }
        catch (SQLException e) {
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
    }
}