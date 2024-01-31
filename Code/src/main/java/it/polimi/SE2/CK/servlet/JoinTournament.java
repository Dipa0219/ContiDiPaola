package it.polimi.SE2.CK.servlet;

import it.polimi.SE2.CK.DAO.TournamentDAO;
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

@WebServlet("/JoinTournament")
@MultipartConfig
public class JoinTournament extends HttpServlet {
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("X-Frame-Options", "DENY"); //do not allow the page to be included in any frame or iframe
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains"); //your application should only be accessible via a secure connection (HTTPS)
        response.setHeader("Content-Security-Policy", "default-src 'self'"); //resources must come from the same source
        response.setHeader("X-Content-Type-Options", "nosniff"); //prevents browsers from interpreting files as anything other than their declared MIME type
        response.setHeader("X-XSS-Protection", "1; mode=block"); //block the page if an XSS attack is detected

        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.getWriter().println("Request non acceptable");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        TournamentDAO tournamentDAO= new TournamentDAO(connection);
        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user.getRole()!= UserRole.STUDENT.getValue()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Educator are not allowed to join tournament in this way, please try again");
            return;
        }
        int tournamentId;
        try {
            tournamentId = Integer.parseInt(StringEscapeUtils.escapeHtml4(request.getParameter("TournamentId")));
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
        Boolean resp;
        try {
            resp=tournamentDAO.joinTournament(user.getId(),tournamentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(!resp){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("You have already joined this tournament");
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}

