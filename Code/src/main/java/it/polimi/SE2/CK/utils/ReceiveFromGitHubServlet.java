package it.polimi.SE2.CK.utils;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.SE2.CK.DAO.BattleDAO;
import it.polimi.SE2.CK.DAO.TeamDAO;
import it.polimi.SE2.CK.bean.Battle;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

//tunneling --> ngrok http http://localhost:8080

/**
 * Servlet that manage all information received from GitHub.
 */
@WebServlet("/receive-from-github")
@MultipartConfig
public class ReceiveFromGitHubServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //request payload
        String payload = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

        //get repository name
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(payload).getAsJsonObject();
        String repositoryName = jsonObject.getAsJsonPrimitive("repository").getAsString();

        //evaluation
        Evaluation evaluation = new Evaluation();
        int point = evaluation.point();

        //get battle name and team name
        String[] resultArray = splitString(repositoryName, "_");
        String battleName = resultArray[0];
        String teamName = resultArray[1];

        //check the existence of battle in the database
        BattleDAO battleDAO = new BattleDAO(connection);
        int battleId;
        try {
            battleId = battleDAO.getBattleId(battleName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Battle battle;
        try {
            battle = battleDAO.showBattleById(battleId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //the battle is not in Ongoing phase
        if (battle.getPhase() != TournamentState.ONGOING) {
            throw new RuntimeException();
        }

        TeamDAO teamDAO = new TeamDAO(connection);
        int teamId = -1;
        try {
            teamId = teamDAO.getTeamId(battleId, teamName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //update point
        boolean result;
        try {
            result = teamDAO.updatePointTeam(teamId, point);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!result){
            throw new RuntimeException();
        }

    }


    /**
     * Divides the input into 2 substring.
     *
     * @param input the input string.
     * @param delimiter the character used by delimiter.
     * @return the 2 substring.
     */
    private static String[] splitString(String input, String delimiter) {
        return input.split(delimiter, 2);
    }
}