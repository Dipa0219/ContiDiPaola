package it.polimi.SE2.CK.servlet;

import it.polimi.SE2.CK.DAO.TournamentDAO;
import it.polimi.SE2.CK.DAO.UserDAO;
import it.polimi.SE2.CK.bean.SessionUser;
import it.polimi.SE2.CK.bean.Tournament;
import it.polimi.SE2.CK.utils.EmailManager;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;
import it.polimi.SE2.CK.utils.enumeration.UserRole;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;

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
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servlet that manage the creation of a tournament.
 */
@WebServlet("/AddCollaborator")
@MultipartConfig
public class AddCollaborator extends HttpServlet {
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

        SessionUser user = (SessionUser) session.getAttribute("user");
        //user is an educator
        //401 error
        if (user.getRole() != UserRole.EDUCATOR.getValue()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("You can't access to this page");
            return;
        }

        //existence of tournament
        TournamentDAO tournamentDAO = new TournamentDAO(connection);
        Tournament tournament = new Tournament();
        tournament.setId(Integer.parseInt(request.getParameter("TournamentID")));
        //500 error
        try {
            tournament = tournamentDAO.showTournamentById(tournament.getId());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        //tournament is in Closed phase
        //406 error
        if (tournament.getPhase().equals(TournamentState.CLOSED.getValue())){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().println("The tournament has already been closed");
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
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        //collaborator list
        String collaborator = request.getParameter("CollaboratorList");
        String[] collaboratorsList = collaborator.split(",");
        ArrayList<String> collaboratorList = new ArrayList<>(Arrays.asList(collaboratorsList));

        //empty collaborator list
        //400 error
        if (collaboratorList.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Choose a collaborator");
            return;
        }

        UserDAO userDAO = new UserDAO(connection);
        //500 error
        for (String s : collaboratorList) {
            try {
                //selected collaborator is a user
                //400 error
                if (userDAO.getUserID(s) == -1) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("You have not selected a valid user");
                    return;
                }
                //selected collaborator is an educator
                //409 error
                if (!userDAO.getUserRole(userDAO.getUserID(s)).equals(UserRole.EDUCATOR)) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().println("You have not selected an educator");
                    return;
                }
                //selected collaborator not is in the tournament
                //409 error
                if (!tournamentDAO.checkUserInTournament(tournament.getId(), userDAO.getUserID(s))){
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().println("You have selected an educator already in the tournament");
                    return;
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("The server do not respond");
                return;
            }
        }

        //add collaborator
        boolean result;
        //500 error
        try {
            result = tournamentDAO.addCollaborator(tournament.getId(), collaboratorList);
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
    }
}
