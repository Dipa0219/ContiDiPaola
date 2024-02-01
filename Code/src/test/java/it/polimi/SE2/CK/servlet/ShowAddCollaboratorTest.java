package it.polimi.SE2.CK.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.SE2.CK.bean.SessionUser;
import junit.framework.TestCase;
import org.junit.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ShowAddCollaboratorTest{

    @Test
    public void test_show_collaborator() throws IOException,ServletException{
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
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
                SessionUser user =new SessionUser();
                user.setId(2);
                user.setUsername("Gary_97");
                user.setRole(0);
                return user;
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

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        when(request.getParameter("TournamentId")).thenReturn("1");

        ArrayList<SessionUser> collaborators = new ArrayList<>();
        SessionUser user = new SessionUser();
        user.setId(3);
        user.setUsername("Josh78");
        user.setRole(0);
        collaborators.add(user);
        // Invoke doGet method
        ShowAddCollaborator showAddCollaborator = new ShowAddCollaborator();
        ServletConfig servletConfig =setUp();
        showAddCollaborator.init(servletConfig);

        showAddCollaborator.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(collaborators);
        assertEquals(writer.toString(), json);
    }

    @Test
    public void test_show_collaborator_with_empty_session() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
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

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Invoke doGet method
        ShowAddCollaborator showAddCollaborator = new ShowAddCollaborator();

        showAddCollaborator.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals(writer.toString(),"You can't access to this page\r\n");
    }

    @Test
    public void test_tournament_id_is_not_number() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
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
                SessionUser user= new SessionUser();
                user.setId(1);
                user.setUsername("Bob99");
                user.setRole(1);
                return user;
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
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("a");


        // Create an instance of ShowBattles and invoke the doGet method
        ShowAddCollaborator showAddCollaborator = new ShowAddCollaborator();
        ServletConfig servletConfig =setUp();
        showAddCollaborator.init(servletConfig);

        showAddCollaborator.doGet(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(writer.toString(),"Internal error with the page, please try again\r\n");
    }

    @Test
    public void test_tournament_id_is_0_or_less() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
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
                SessionUser user= new SessionUser();
                user.setId(1);
                user.setUsername("Bob99");
                user.setRole(1);
                return user;
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
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("-1");


        // Create an instance of ShowBattles and invoke the doGet method
        ShowAddCollaborator showAddCollaborator = new ShowAddCollaborator();
        ServletConfig servletConfig =setUp();
        showAddCollaborator.init(servletConfig);

        showAddCollaborator.doGet(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(writer.toString(),"Internal error with the page, please try again\r\n");
    }

    @Test
    public void test_show_collaborator_with_wrong_role() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
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
                SessionUser user= new SessionUser();
                user.setId(1);
                user.setUsername("Bob99");
                user.setRole(1);
                return user;
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

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(request.getParameter("TournamentId")).thenReturn("1");

        // Invoke doGet method
        ShowAddCollaborator showAddCollaborator = new ShowAddCollaborator();
        ServletConfig servletConfig =setUp();
        showAddCollaborator.init(servletConfig);

        showAddCollaborator.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals(writer.toString(),"You can't do this action\r\n");
    }

    @Test
    public void test_show_collaborator_wrong_educator() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
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
                SessionUser user =new SessionUser();
                user.setId(3);
                user.setUsername("Josh78");
                user.setRole(0);
                return user;
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

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        when(request.getParameter("TournamentId")).thenReturn("1");

        // Invoke doGet method
        ShowAddCollaborator showAddCollaborator = new ShowAddCollaborator();
        ServletConfig servletConfig =setUp();
        showAddCollaborator.init(servletConfig);

        showAddCollaborator.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals(writer.toString(),"You can't access to this page\r\n");
    }

    @Test
    public void test_show_collaborator_closed_tournament() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
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
                SessionUser user =new SessionUser();
                user.setId(9);
                user.setUsername("VivaVerdi");
                user.setRole(0);
                return user;
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

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        when(request.getParameter("TournamentId")).thenReturn("2");

        // Invoke doGet method
        ShowAddCollaborator showAddCollaborator = new ShowAddCollaborator();
        ServletConfig servletConfig =setUp();
        showAddCollaborator.init(servletConfig);

        showAddCollaborator.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals(writer.toString(),"The tournament has already been closed\r\n");
    }

    @Test
    public void test_getUser_doPost() throws IOException, ServletException {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Set the parameters for the request
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Create an instance of LoginManager and invoke the doPost method
        ShowAddCollaborator showAddCollaborator = new ShowAddCollaborator();

        showAddCollaborator.doPost(request, response);

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
        when(servletContext.getInitParameter("dbUrl")).thenReturn("jdbc:mysql://localhost:3306/ckbtest?serverTimezone=UTC");
        when(servletContext.getInitParameter("dbUser")).thenReturn("root");
        when(servletContext.getInitParameter("dbPassword")).thenReturn("");
        when(servletContext.getInitParameter("dbDriver")).thenReturn("com.mysql.cj.jdbc.Driver");
        return  servletConfig;
    }
}