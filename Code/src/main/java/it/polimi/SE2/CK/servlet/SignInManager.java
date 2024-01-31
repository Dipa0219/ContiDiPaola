package it.polimi.SE2.CK.servlet;

import it.polimi.SE2.CK.DAO.UserDAO;
import it.polimi.SE2.CK.bean.User;
import it.polimi.SE2.CK.utils.EmailManager;
import org.apache.commons.lang3.StringUtils;
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
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("X-Frame-Options", "DENY"); //do not allow the page to be included in any frame or iframe
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains"); //your application should only be accessible via a secure connection (HTTPS)
        response.setHeader("Content-Security-Policy", "default-src 'self'"); //resources must come from the same source
        response.setHeader("X-Content-Type-Options", "nosniff"); //prevents browsers from interpreting files as anything other than their declared MIME type
        response.setHeader("X-XSS-Protection", "1; mode=block"); //block the page if an XSS attack is detected

        if (StringUtils.isAnyEmpty(request.getParameter("role"),request.getParameter("name"),request.getParameter("surname"),
                request.getParameter("birthdate"),request.getParameter("SignInUsername"), request.getParameter("email"),
                request.getParameter("SignInPassword"), request.getParameter("userGH"))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("All fields are required");
            return;
        }
        User user= new User();
        try {
            user.setRole(Integer.parseInt(StringEscapeUtils.escapeHtml4(request.getParameter("role"))));
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("An error occurred with the data sent, please retry");
            return;
        }
        user.setName(StringEscapeUtils.escapeHtml4(request.getParameter("name")));
        user.setSurname(StringEscapeUtils.escapeHtml4(request.getParameter("surname")));
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        try {
            user.setBirthdate(new Date(date.parse(StringEscapeUtils.escapeHtml4(request.getParameter("birthdate"))).getTime()));
        } catch (ParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("You must insert a date in the field birthdate, please retry");
            return;
        }
        user.setUsername(StringEscapeUtils.escapeHtml4(request.getParameter("SignInUsername")));

        //check if the email structure is correct
        if (!EmailManager.isValidEmail(StringEscapeUtils.escapeHtml4(request.getParameter("email")))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("You must insert a valid email, please retry");
            return;
        }

        user.setEmail(StringEscapeUtils.escapeHtml4(request.getParameter("email")));
        user.setPassword(StringEscapeUtils.escapeHtml4(request.getParameter("SignInPassword")));
        if (!user.getPassword().equals(StringEscapeUtils.escapeHtml4(request.getParameter("ConfirmPassword")))){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("The two password must be the same, please retry");
            return;
        }
        user.setGitHubUser(StringEscapeUtils.escapeHtml4(request.getParameter("userGH")));
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
