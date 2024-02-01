package it.polimi.SE2.CK.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.SE2.CK.DAO.BattleDAO;
import it.polimi.SE2.CK.DAO.TournamentDAO;
import it.polimi.SE2.CK.bean.Battle;
import it.polimi.SE2.CK.bean.Ranking;
import it.polimi.SE2.CK.bean.Tournament;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.RequestDispatcher;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CheckBattleRanking")
@MultipartConfig
public class CheckBattleRanking extends HttpServlet {
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
        response.setHeader("X-Frame-Options", "DENY"); //do not allow the page to be included in any frame or iframe
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains"); //your application should only be accessible via a secure connection (HTTPS)
        response.setHeader("Content-Security-Policy", "default-src 'self'"); //resources must come from the same source
        response.setHeader("X-Content-Type-Options", "nosniff"); //prevents browsers from interpreting files as anything other than their declared MIME type
        response.setHeader("X-XSS-Protection", "1; mode=block"); //block the page if an XSS attack is detected

        HttpSession session = request.getSession();
        if(session.isNew() || session.getAttribute("user")==null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("You can't access to this page");
            return;
        }
        int battleId;
        try {
            battleId = Integer.parseInt(StringEscapeUtils.escapeHtml4(request.getParameter("BattleId")));
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Internal error with the page, please try again");
            return;
        }
        if (battleId<=0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Internal error with the page, please try again");
            return;
        }

        BattleDAO battleDAO= new BattleDAO(connection);
        Battle battle;
        try {
            battle= battleDAO.showBattleById(battleId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(battle==null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String,String> res= new HashMap<>();
            res.put("NotStarted","There isn't any battle with the given id, please try with an other one");
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(res);
            response.getWriter().write(json);
            return;
        }
        if(battle.getPhase().equals(TournamentState.NOTSTARTED)){
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
            rankings=battleDAO.showRanking(battleId);
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
}
