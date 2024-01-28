package it.polimi.SE2.CK.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.SE2.CK.DAO.TournamentDAO;
import it.polimi.SE2.CK.bean.SessionUser;
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

@WebServlet("/ShowJoinTournament")
@MultipartConfig
public class ShowJoinTournament extends HttpServlet {
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
        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user.getRole()!= UserRole.STUDENT.getValue()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("You can't do this action");
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
        Boolean resp;
        try {
            resp = tournamentDAO.checkUserInTournament(tournamentId,user.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(resp);
        response.getWriter().write(json);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.getWriter().println("Request non acceptable");
    }
}
