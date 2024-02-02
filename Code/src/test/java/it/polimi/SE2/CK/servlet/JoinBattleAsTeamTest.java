package it.polimi.SE2.CK.servlet;


import it.polimi.SE2.CK.TestUtils;
import it.polimi.SE2.CK.bean.SessionUser;
import org.junit.Test;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JoinBattleAsTeamTest {

    TestUtils testUtils = new TestUtils();

    @Test
    public void test_join_team_partial() throws IOException, ServletException {
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
                user.setId(5);
                user.setUsername("Jean00");
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

        String[] list= new String[1];
        list[0]="6";
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("6");
        when(request.getParameterValues("teamMateInput")).thenReturn(list);
        when(request.getParameter("teamNameInput")).thenReturn("Boomers");

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
    }

    @Test
    public void test_join_team_incomplete() throws IOException, ServletException {
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
                user.setId(5);
                user.setUsername("Jean00");
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

        String[] list= new String[1];
        list[0]="6";
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("7");
        when(request.getParameterValues("teamMateInput")).thenReturn(list);
        when(request.getParameter("teamNameInput")).thenReturn("Boomers");

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
    }

    @Test
    public void test_join_battle_doGet() throws IOException {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Set the parameters for the request
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(request.getRequestDispatcher("ErrorPage.html")).thenReturn(new RequestDispatcher() {
            @Override
            public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

            }

            @Override
            public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

            }
        });
        // Create an instance of LoginManager and invoke the doPost method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();

        joinBattleAsTeam.doGet(request, response);

        // Verify that the response status is 400
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        // Verify that the error message is written to the response
        assertEquals (writer.toString(),"Request non acceptable\r\n");
    }

    @Test
    public void test_join_battle_with_empty_session() throws IOException {
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
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();

        joinBattleAsTeam.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals(writer.toString(),"You can't access to this page\r\n");
    }

    @Test
    public void test_battle_id_is_not_number() throws IOException {
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
        when(request.getParameter("BattleId")).thenReturn("a");


        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();

        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(writer.toString(),"Internal error with the page, please try again\r\n");
    }

    @Test
    public void test_battle_id_is_0_or_less() throws IOException {
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
        when(request.getParameter("BattleId")).thenReturn("-1");


        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();

        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(writer.toString(),"Internal error with the page, please try again\r\n");
    }

    @Test
    public void test_user_is_not_a_student() throws IOException {
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
                user.setId(2);
                user.setUsername("David86");
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

        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("1");

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();

        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals(writer.toString(),"You can't access to this page\r\n");
    }

    @Test
    public void test_battle_is_already_started() throws IOException, ServletException {
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
        when(request.getParameter("BattleId")).thenReturn("1");

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        assertEquals(writer.toString(),"The battle has already begun\r\n");
    }

    @Test
    public void test_battle_does_not_exist() throws IOException, ServletException {
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
        when(request.getParameter("BattleId")).thenReturn("444");

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        assertEquals("The battle doesn't exist\r\n", writer.toString());
    }

    @Test
    public void test_teamlist_is_null() throws IOException, ServletException {
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
        when(request.getParameter("BattleId")).thenReturn("6");
        when(request.getParameter("teamMateInput")).thenReturn(null);

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("You have to choose a teammate\r\n", writer.toString());
    }

    @Test
    public void test_teamlist_is_too_long() throws IOException, ServletException {
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
                user.setId(5);
                user.setUsername("Jean00");
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

        String[] list= new String[4];
        list[0]="2";
        list[1]="2";
        list[2]="2";
        list[3]="2";
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("6");
        when(request.getParameterValues("teamMateInput")).thenReturn(list);

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("You have to choose a number of teammates between 1 and 2\r\n", writer.toString());
    }

    @Test
    public void test_teamlist_contains_collaborator() throws IOException, ServletException {
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
                user.setId(5);
                user.setUsername("Jean00");
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

        String[] list= new String[1];
        list[0]="2";
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("6");
        when(request.getParameterValues("teamMateInput")).thenReturn(list);

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        assertEquals("You have to choose a student as teammate\r\n", writer.toString());
    }

    @Test
    public void test_teamlist_contains_student_in_an_other_team() throws IOException, ServletException {
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
                user.setId(5);
                user.setUsername("Jean00");
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

        String[] list= new String[1];
        list[0]="5";
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("5");
        when(request.getParameterValues("teamMateInput")).thenReturn(list);

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
        assertEquals("You select a student that is already signed up to another team\r\n", writer.toString());
    }

    @Test
    public void test_user_is_already_in_a_team() throws IOException, ServletException {
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

        String[] list= new String[1];
        list[0]="6";
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("4");
        when(request.getParameterValues("teamMateInput")).thenReturn(list);

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("You can't access to this page\r\n", writer.toString());
    }

    @Test
    public void test_team_name_is_empty() throws IOException, ServletException {
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
                user.setId(5);
                user.setUsername("Jean00");
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

        String[] list= new String[1];
        list[0]="6";
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("4");
        when(request.getParameterValues("teamMateInput")).thenReturn(list);

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("You have to choose a name for the team\r\n", writer.toString());
    }

    @Test
    public void test_team_name_is_already_used() throws IOException, ServletException {
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
                user.setId(5);
                user.setUsername("Jean00");
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

        String[] list= new String[1];
        list[0]="6";
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("4");
        when(request.getParameterValues("teamMateInput")).thenReturn(list);
        when(request.getParameter("teamNameInput")).thenReturn("Bombers");

        // Create an instance of ShowBattles and invoke the doGet method
        JoinBattleAsTeam joinBattleAsTeam = new JoinBattleAsTeam();
        ServletConfig servletConfig = testUtils.setUp();
        joinBattleAsTeam.init(servletConfig);
        joinBattleAsTeam.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
        assertEquals("The name is already in use for the battle\r\n", writer.toString());
    }

}