package it.polimi.SE2.CK.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.SE2.CK.DAO.BattleDAO;
import it.polimi.SE2.CK.DAO.TournamentDAO;
import it.polimi.SE2.CK.DAO.UserDAO;
import it.polimi.SE2.CK.bean.Battle;
import it.polimi.SE2.CK.bean.SessionUser;
import it.polimi.SE2.CK.bean.Tournament;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;
import it.polimi.SE2.CK.utils.enumeration.UserRole;

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
import java.util.ArrayList;

@WebServlet("/ShowAddCollaborator")
@MultipartConfig
public class ShowAddCollaborator extends HttpServlet {
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
            ServletContext context = getServletContext();
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        //the user is authorized or not
        //401 error
        if(session.isNew() || session.getAttribute("user")==null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("You can't access to this page");
            return;
        }

        //existence of tournament
        TournamentDAO tournamentDAO = new TournamentDAO(connection);
        Tournament tournament = new Tournament();
        try {
            tournament.setId(Integer.parseInt(request.getParameter("TournamentId")));
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Internal error with the page, please try again");
            return;
        }
        if (tournament.getId()<=0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Internal error with the page, please try again");
            return;
        }
        //500 error
        try {
            tournament = tournamentDAO.showTournamentById(tournament.getId());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }
        SessionUser user = (SessionUser) session.getAttribute("user");
        //401 error
        if (user.getRole() != UserRole.EDUCATOR.getValue()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("You can't do this action");
            return;
        }

        //tournament is in not in Closed phase
        //401 error
        if (tournament.getPhase().equals(TournamentState.CLOSED.getValue())){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("The tournament has already been closed");
            return;
        }

        //user is an educator


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

        //get educator not in tournament
        ArrayList<String> educatorNotInTournament = new ArrayList<>();
        //500 error
        try{
            educatorNotInTournament = (ArrayList<String>) tournamentDAO.showEducatorNotInTournament(tournament.getId());
        }
        catch (SQLException e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("The server do not respond");
            return;
        }

        //200 ok
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(educatorNotInTournament);
        response.getWriter().write(json);
    }

    /**
     * Called by the server (via the service method) to allow a servlet to handle a POST request.
     *
     * @param request object that contains the request the client has made of the servlet
     * @param response object that contains the response the client has made of the servlet
     * @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.getWriter().println("Request non acceptable");
    }
}
