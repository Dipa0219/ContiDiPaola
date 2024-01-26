package it.polimi.SE2.CK.servlet;

import it.polimi.SE2.CK.DAO.UserDAO;
import it.polimi.SE2.CK.bean.User;
import org.apache.commons.lang3.StringUtils;

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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static java.lang.Integer.getInteger;

@WebServlet("/SignInManager")
@MultipartConfig
public class SignInManager extends HttpServlet {
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (StringUtils.isAnyEmpty(request.getParameter("role"),request.getParameter("name"),request.getParameter("surname"),
                request.getParameter("birthdate"),request.getParameter("SignInUsername"), request.getParameter("email"),
                request.getParameter("SignInPassword"), request.getParameter("userGH"))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("All fields are required");
            return;
        }
        User user= new User();
        try {
            user.setRole(Integer.parseInt(request.getParameter("role")));
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("An error occurred with the data sent, please retry");
            return;
        }
        user.setName(request.getParameter("name"));
        user.setSurname(request.getParameter("surname"));
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        try {
            user.setBirthdate(new Date(date.parse(request.getParameter("birthdate")).getTime()));
        } catch (ParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("You must insert a date in the field birthdate, please retry");
            return;
        }
        user.setUsername(request.getParameter("SignInUsername"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("SignInPassword"));
        if (!user.getPassword().equals(request.getParameter("ConfirmPassword"))){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("The two password must be the same, please retry");
            return;
        }
        user.setGitHubUser(request.getParameter("userGH"));
        UserDAO userDAO= new UserDAO(connection);
        int res;
        try {
            res= userDAO.createUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (res==1){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Username already used");
            return;
        } else if (res==2) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Email already used");
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}
