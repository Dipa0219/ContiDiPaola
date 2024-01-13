package it.polimi.tiw.jdbctest.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import it.polimi.tiw.jdbctest.bean.User;
import it.polimi.tiw.jdbctest.DAO.UserDAO;

@WebServlet ("/LoginManager")
@MultipartConfig
public class LoginManager extends HttpServlet {
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
        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.getWriter().println("Request non acceptable");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usrn = null;
        String pwd = null;
        usrn = request.getParameter("LoginUsername");
        pwd = request.getParameter("LoginPassword");
        if (StringUtils.isAnyEmpty(usrn,pwd)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Credentials must be not null");
            return;
        }
        // query db to authenticate for user
        UserDAO userDao = new UserDAO(connection);
        int userId=0;
        try {
            userId = userDao.checkUsername(usrn, pwd);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error, retry later");
            return;
        }
        // If the user exists, add info to the session and go to home page, otherwise
        // return an error status code and message
        if (userId == 0) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Incorrect credentials");
        } else {
            request.getSession().setAttribute("user", userId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(userId);
        }
    }
}
