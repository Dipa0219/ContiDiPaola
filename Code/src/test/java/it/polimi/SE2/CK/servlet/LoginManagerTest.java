package it.polimi.SE2.CK.servlet;
// Generated by CodiumAI

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.SE2.CK.DAO.UserDAO;
import it.polimi.SE2.CK.bean.SessionUser;
import it.polimi.SE2.CK.servlet.LoginManager;

import org.junit.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginManagerTest {


    // Login with correct credentials returns status 200 and user info in JSON format
    @Test
    public void test_login_with_correct_credentials() throws ServletException, IOException {
        // Mock HttpServletRequest and HttpServletResponse

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter writer = new StringWriter();
        // Set the parameters for the request
        when(request.getSession()).thenReturn(new HttpSession() {
            @Override
            public long getCreationTime() {
                return 0;
            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public long getLastAccessedTime() {
                return 0;
            }

            @Override
            public ServletContext getServletContext() {
                return null;
            }

            @Override
            public void setMaxInactiveInterval(int i) {

            }

            @Override
            public int getMaxInactiveInterval() {
                return 0;
            }

            @Override
            public HttpSessionContext getSessionContext() {
                return null;
            }

            @Override
            public Object getAttribute(String s) {
                return null;
            }

            @Override
            public Object getValue(String s) {
                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                return null;
            }

            @Override
            public String[] getValueNames() {
                return new String[0];
            }

            @Override
            public void setAttribute(String s, Object o) {

            }

            @Override
            public void putValue(String s, Object o) {

            }

            @Override
            public void removeAttribute(String s) {

            }

            @Override
            public void removeValue(String s) {

            }

            @Override
            public void invalidate() {

            }

            @Override
            public boolean isNew() {
                return false;
            }
        });
        when(request.getParameter("LoginUsername")).thenReturn("Bob99");
        when(request.getParameter("LoginPassword")).thenReturn("1");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Mock the UserDAO and SessionUser objects
        UserDAO userDao = mock(UserDAO.class);
        SessionUser user = new SessionUser();
        user.setId(1);
        user.setUsername("Bob99");
        user.setRole(1);
    
        // Mock the database connection
        Connection connection = mock(Connection.class);
    
        // Mock the checkUsername method to return the user object
        try {
            when(userDao.checkUsername("Bob99", "1")).thenReturn(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        // Create an instance of LoginManager and invoke the doPost method
        LoginManager loginManager = new LoginManager();
        // Mock servlet config
        ServletConfig servletConfig = setUp();
        loginManager.init(servletConfig);
        loginManager.doPost(request, response);
    
        // Verify that the response status is 200
        verify(response).setStatus(HttpServletResponse.SC_OK);
    
        // Verify that the response content type is application/json
        verify(response).setContentType("application/json");
    
        // Verify that the response character encoding is UTF-8
        verify(response).setCharacterEncoding("UTF-8");
    
        // Verify that the user object is added to the session
        //verify(request.getSession()).setAttribute("user", user);
    
        // Verify that the user object is converted to JSON and written to the response
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(user);
        assert (writer.toString().equals(json));
    }

    // Login with incorrect credentials returns status 401 and error message
    @Test
    public void test_login_with_incorrect_credentials() throws ServletException, IOException {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
    
        // Set the parameters for the request
        when(request.getParameter("LoginUsername")).thenReturn("username");
        when(request.getParameter("LoginPassword")).thenReturn("password");
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    
        // Mock the UserDAO object
        UserDAO userDao = mock(UserDAO.class);
    
        // Mock the database connection
        Connection connection = mock(Connection.class);
    
        // Mock the checkUsername method to return null
        try {
            when(userDao.checkUsername("username", "password")).thenReturn(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        // Create an instance of LoginManager and invoke the doPost method
        LoginManager loginManager = new LoginManager();
        ServletConfig servletConfig = setUp();
        loginManager.init(servletConfig);
        loginManager.doPost(request, response);
    
        // Verify that the response status is 401
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    
        // Verify that the error message is written to the response
        assertEquals(writer.toString(),"Incorrect credentials\r\n");
    }


    // Login with empty username or password returns status 400 and error message
    @Test
    public void test_login_with_empty_credentials() throws ServletException, IOException {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
    
        // Set the parameters for the request
        when(request.getParameter("LoginUsername")).thenReturn("");
        when(request.getParameter("LoginPassword")).thenReturn("");
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    
        // Create an instance of LoginManager and invoke the doPost method
        LoginManager loginManager = new LoginManager();

        // Mock servlet config
        ServletConfig servletConfig = setUp();
        loginManager.init(servletConfig);
        loginManager.doPost(request, response);
    
        // Verify that the response status is 400
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        // Verify that the error message is written to the response
        assertEquals (writer.toString(),"Credentials must be not null\r\n");
    }

    @Test
    public void test_login_doGet() throws ServletException, IOException {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Set the parameters for the request
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Create an instance of LoginManager and invoke the doPost method
        LoginManager loginManager = new LoginManager();

        loginManager.doGet(request, response);

        // Verify that the response status is 400
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        // Verify that the error message is written to the response
        assertEquals (writer.toString(),"Request non acceptable\r\n");
    }

    private ServletConfig setUp() {
        ServletContext servletContext = mock(ServletContext.class);

        // Mock servlet config
        ServletConfig servletConfig = mock(ServletConfig.class);
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter("dbUrl")).thenReturn("jdbc:mysql://localhost:3306/new_schema?serverTimezone=UTC");
        when(servletContext.getInitParameter("dbUser")).thenReturn("root");
        when(servletContext.getInitParameter("dbPassword")).thenReturn("");
        when(servletContext.getInitParameter("dbDriver")).thenReturn("com.mysql.cj.jdbc.Driver");
        return  servletConfig;
    }
}