package it.polimi.SE2.CK.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.SE2.CK.DAO.TournamentDAO;
import it.polimi.SE2.CK.bean.Ranking;
import it.polimi.SE2.CK.bean.Tournament;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;

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
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CheckTournamentRanking")
@MultipartConfig
public class CheckTournamentRanking extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session.isNew() || session.getAttribute("user")==null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("You can't access to this page");
            return;
        }
        int tournamentId;
        try {
            tournamentId = Integer.parseInt(request.getParameter("TournamentId"));
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Internal error with the page, please try again");
            return;
        }
        if (tournamentId<=0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Internal error with the page, please try again");
            return;
        }

        TournamentDAO tournamentDAO= new TournamentDAO(connection);
        Tournament tournament;
        try {
            tournament= tournamentDAO.showTournamentById(tournamentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(tournament==null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String,String> res= new HashMap<>();
            res.put("NotStarted","There isn't any tournament with the given id, please try with an other one");
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(res);
            response.getWriter().write(json);
            return;
        }
        if(tournament.getPhase().equals(TournamentState.NOTSTARTED.getValue())){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String,String> res= new HashMap<>();
            res.put("NotStarted","1");
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(res);
            response.getWriter().write(json);
            return;
        }
        ArrayList<Ranking> rankings;
        try {
            rankings=tournamentDAO.showRanking(tournamentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(rankings);
        response.getWriter().write(json);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.getWriter().println("Request non acceptable");
    }
}