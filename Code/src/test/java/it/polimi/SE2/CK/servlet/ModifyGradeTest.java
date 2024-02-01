package it.polimi.SE2.CK.servlet;

import it.polimi.SE2.CK.bean.SessionUser;
import junit.framework.TestCase;
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
import static org.mockito.Mockito.when;

public class ModifyGradeTest {

    @Test
    public void test_show_team_doGet() throws IOException{
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
        ModifyGrade modifyGrade = new ModifyGrade();

        modifyGrade.doGet(request, response);

        // Verify that the response status is 400
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        // Verify that the error message is written to the response
        assertEquals (writer.toString(),"Request non acceptable\r\n");
    }

    @Test
    public void test_modify_grade() throws IOException, ServletException {
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
                user.setId(4);
                user.setUsername("Ted69");
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
        when(request.getParameter("BattleId")).thenReturn("2");
        when(request.getParameter("teamGradeInput")).thenReturn("1");
        when(request.getParameter("teamGraduationInput")).thenReturn("7");


        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();
        ServletConfig servletConfig =setUp();
        modifyGrade.init(servletConfig);

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
    }

    @Test
    public void test_search_tournament_with_empty_session() throws IOException {
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
        ModifyGrade modifyGrade = new ModifyGrade();

        modifyGrade.doPost(request, response);
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
        ModifyGrade modifyGrade = new ModifyGrade();

        modifyGrade.doPost(request, response);

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
        ModifyGrade modifyGrade = new ModifyGrade();

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(writer.toString(),"Internal error with the page, please try again\r\n");
    }

    @Test
    public void test_team_id_is_not_number() throws IOException {
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
        when(request.getParameter("TeamId")).thenReturn("a");


        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(writer.toString(),"Internal error with the page, please try again\r\n");
    }

    @Test
    public void test_team_id_is_0_or_less() throws IOException {
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
        when(request.getParameter("TeamId")).thenReturn("-1");

        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(writer.toString(),"Internal error with the page, please try again\r\n");
    }

    @Test
    public void test_team_points_is_not_number() throws IOException {
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
        when(request.getParameter("teamGradeInput")).thenReturn("1");
        when(request.getParameter("teamGraduationInput")).thenReturn("a");

        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(writer.toString(),"Points must be a number\r\n");
    }

    @Test
    public void test_team_points_is_0_or_less() throws IOException {
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
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("1");
        when(request.getParameter("teamGradeInput")).thenReturn("1");
        when(request.getParameter("teamGraduationInput")).thenReturn("-1");

        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(writer.toString(),"Points must be between 0 and 100\r\n");
    }

    @Test
    public void test_team_points_is_101_or_more() throws IOException {
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
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("1");
        when(request.getParameter("teamGradeInput")).thenReturn("1");
        when(request.getParameter("teamGraduationInput")).thenReturn("101");

        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(writer.toString(),"Points must be between 0 and 100\r\n");
    }

    @Test
    public void test_modify_grade_user_is_not_educator() throws IOException {
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
        // Set up the request parameters
        when(request.getParameter("BattleId")).thenReturn("1");
        when(request.getParameter("teamGradeInput")).thenReturn("1");
        when(request.getParameter("teamGraduationInput")).thenReturn("5");

        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("You can't access to this page\r\n",writer.toString());
    }

    @Test
    public void test_battle_id_not_exists() throws IOException, ServletException {
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
                user.setUsername("MarielloBello");
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
        when(request.getParameter("BattleId")).thenReturn("45");
        when(request.getParameter("teamGradeInput")).thenReturn("1");
        when(request.getParameter("teamGraduationInput")).thenReturn("5");


        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();
        ServletConfig servletConfig =setUp();
        modifyGrade.init(servletConfig);

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        assertEquals("The battle doesn't exist\r\n",writer.toString());
    }

    @Test
    public void test_battle_is_not_ongoing() throws IOException, ServletException {
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
                user.setUsername("MarielloBello");
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
        when(request.getParameter("BattleId")).thenReturn("3");
        when(request.getParameter("teamGradeInput")).thenReturn("1");
        when(request.getParameter("teamGraduationInput")).thenReturn("5");


        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();
        ServletConfig servletConfig =setUp();
        modifyGrade.init(servletConfig);

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        assertEquals("This action is not possible in this moment\r\n",writer.toString());
    }

    @Test
    public void test_user_is_not_a_collaborator() throws IOException, ServletException {
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
                user.setUsername("David87");
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
        when(request.getParameter("BattleId")).thenReturn("2");
        when(request.getParameter("teamGradeInput")).thenReturn("1");
        when(request.getParameter("teamGraduationInput")).thenReturn("5");


        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();
        ServletConfig servletConfig =setUp();
        modifyGrade.init(servletConfig);

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        assertEquals("You must be a collaborator to modify grade\r\n",writer.toString());
    }

    @Test
    public void test_team_belong_to_an_other_battle() throws IOException, ServletException {
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
                user.setId(4);
                user.setUsername("Ted69");
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
        when(request.getParameter("BattleId")).thenReturn("2");
        when(request.getParameter("teamGradeInput")).thenReturn("2");
        when(request.getParameter("teamGraduationInput")).thenReturn("5");


        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();
        ServletConfig servletConfig =setUp();
        modifyGrade.init(servletConfig);

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        assertEquals("You can't modify this team evaluation or this team doesn't exists\r\n",writer.toString());
    }

    @Test
    public void test_team_does_not_exists() throws IOException, ServletException {
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
                user.setId(4);
                user.setUsername("Ted69");
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
        when(request.getParameter("BattleId")).thenReturn("2");
        when(request.getParameter("teamGradeInput")).thenReturn("455");
        when(request.getParameter("teamGraduationInput")).thenReturn("5");


        // Create an instance of ShowBattles and invoke the doGet method
        ModifyGrade modifyGrade = new ModifyGrade();
        ServletConfig servletConfig =setUp();
        modifyGrade.init(servletConfig);

        modifyGrade.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        assertEquals("You can't modify this team evaluation or this team doesn't exists\r\n",writer.toString());
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