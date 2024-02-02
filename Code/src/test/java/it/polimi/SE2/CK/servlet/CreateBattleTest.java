package it.polimi.SE2.CK.servlet;

import it.polimi.SE2.CK.TestUtils;
import it.polimi.SE2.CK.bean.SessionUser;
import junit.framework.TestCase;
import org.junit.Test;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CreateBattleTest {
    TestUtils testUtils =new TestUtils();
    @Test
    public void test_create_battle_info_doGet() throws IOException{
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
        CreateBattle createBattle = new CreateBattle();

        createBattle.doGet(request, response);

        // Verify that the response status is 400
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        // Verify that the error message is written to the response
        assertEquals (writer.toString(),"Request non acceptable\r\n");
    }

    @Test
    public void test_create_battle() throws IOException, ServletException {
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
                user.setUsername("Tom69");
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
        Part part = mock(Part.class);
        File file =new File("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        // Mock the InputStream of the Part
        InputStream inputStream = new FileInputStream(file);
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        File file2 =new File("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\yaml.yaml");
        InputStream testInputStream = new FileInputStream(file);
        when(testPart.getInputStream()).thenReturn(testInputStream);
        when(testPart.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"yaml.yaml\"");
        long l = 1;
        when(testPart.getSize()).thenReturn(l);
        Random random= new Random();
        int numeroInteroCasuale = random.nextInt();

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn(Integer.toString(numeroInteroCasuale));
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-11-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();
        ServletConfig servletConfig = testUtils.setUp();
        createBattle.init(servletConfig);
        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
    }

    @Test
    public void test_create_battle_with_empty_session() throws IOException{
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
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);
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
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Internal error with the page, please try again\r\n", writer.toString());
    }

    @Test
    public void test_battle_id_is_0_or_less() throws IOException, ServletException {
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
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Internal error with the page, please try again\r\n", writer.toString());
    }

    @Test
    public void test_min_num_id_is_not_number() throws IOException, ServletException {
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
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("a");

        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("You must insert a number in minimum number of student\r\n", writer.toString());
    }

    @Test
    public void test_max_num_id_is_not_number() throws IOException, ServletException {
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
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("a");

        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("You must insert a number in maximum number of student\r\n", writer.toString());
    }

    @Test
    public void test_min_num_id_is_0_or_less() throws IOException {
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
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("-1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");

        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Minimum and maximum number of student bust be greater than 0\r\n", writer.toString());
    }

    @Test
    public void test_max_num_id_is_0_or_less() throws IOException{
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
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("-1");

        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Minimum and maximum number of student bust be greater than 0\r\n",writer.toString());
    }

    @Test
    public void test_min_is_greater_than_max() throws IOException {
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
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("11");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");

        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Minimum number of student can't be greater than maximum\r\n", writer.toString());
    }

    @Test
    public void test_project_not_existing() throws IOException {
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
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");

        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("All fields with an asterisk are required\r\n", writer.toString());
    }

    @Test
    public void test_test_not_existing() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("src/main/java/it/polimi/SE2/CK/testElements/Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getPart("battleProject")).thenReturn(part);

        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("All fields with an asterisk are required\r\n", writer.toString());
    }

    @Test
    public void test_empty_battle_name() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("src/main/java/it/polimi/SE2/CK/testElements/Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("src/main/java/it/polimi/SE2/CK/testElements/Test.zip");
        when(testPart.getInputStream()).thenReturn(testInputStream);
        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);

        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("All fields with an asterisk are required\r\n", writer.toString());
    }

    @Test
    public void test_empty_reg_date() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("src/main/java/it/polimi/SE2/CK/testElements/Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("src/main/java/it/polimi/SE2/CK/testElements/Test.zip");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);
        when(request.getParameter("battleNameInput")).thenReturn("Ciao");

        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("All fields with an asterisk are required\r\n", writer.toString());
    }

    @Test
    public void test_empty_sub_date() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("src/main/java/it/polimi/SE2/CK/testElements/Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("src/main/java/it/polimi/SE2/CK/testElements/Test.zip");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);
        when(request.getParameter("battleNameInput")).thenReturn("Ciao");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("Ciao");

        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("All fields with an asterisk are required\r\n", writer.toString());
    }

    @Test
    public void test_empty_file_name() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Ciaoffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("Ciao");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("Ciao");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("The max length of battle name is 45 character\r\n", writer.toString());
    }

    @Test
    public void test_too_long_battle_name() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("1");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Ciaoffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("Ciao");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("Ciao");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("The max length of battle name is 45 character\r\n", writer.toString());
    }

    @Test
    public void test_too_long_battle_description() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Iterate");
        when(request.getParameter("battleDescriptionInput")).thenReturn("Iteratefffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("Ciao");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("Ciao");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("The max length of battle description is 200 character\r\n", writer.toString());
    }

    @Test
    public void test_too_long_reg_date() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Iterate");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("22222-12-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("Ciao");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("You must insert a valid date\r\n", writer.toString());
    }

    @Test
    public void test_too_long_sub_date() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);
        //when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"yaml.yaml\"");

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Iterate");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("20244-12-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("You must insert a valid date\r\n", writer.toString());
    }

    @Test
    public void test_not_valid_reg_date() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Iterate");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("a");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Insert a valid date\r\n", writer.toString());
    }

    @Test
    public void test_not_valid_sub_date() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);
        //when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"yaml.yaml\"");

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Iterate");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("a");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Insert a valid date\r\n", writer.toString());
    }

    @Test
    public void test_reg_is_too_early() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Iterate");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2022-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-09-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Registration deadline must be after now\r\n", writer.toString());
    }

    @Test
    public void test_sub_is_too_early() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Iterate");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2022-09-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Submission deadline must be after now\r\n", writer.toString());
    }

    @Test
    public void test_reg_after_sub() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);
        //when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"yaml.yaml\"");

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Iterate");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-09-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();

        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Registration deadline must be before submission deadline\r\n", writer.toString());
    }

    @Test
    public void test_battle_name_already_used() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Iterate");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-11-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();
        ServletConfig servletConfig = testUtils.setUp();
        createBattle.init(servletConfig);
        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
        assertEquals("Existing battle name, choose another one\r\n", writer.toString());
    }

    @Test
    public void test_file_is_not_a_zip() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.jsp\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.yaml");
        when(testPart.getInputStream()).thenReturn(testInputStream);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Execute");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-11-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();
        ServletConfig servletConfig = testUtils.setUp();
        createBattle.init(servletConfig);
        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Insert a valid project\r\n", writer.toString());
    }

    @Test
    public void test_test_is_not_a_yaml() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.txt");
        when(testPart.getInputStream()).thenReturn(testInputStream);
        when(testPart.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"yaml.zip\"");
        long l = 1;
        when(testPart.getSize()).thenReturn(l);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Execute");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-11-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();
        ServletConfig servletConfig = testUtils.setUp();
        createBattle.init(servletConfig);
        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Insert a valid test case\r\n", writer.toString());
    }

    @Test
    public void test_tournament_does_not_exist() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.txt");
        when(testPart.getInputStream()).thenReturn(testInputStream);
        when(testPart.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"yaml.yaml\"");
        long l = 1;
        when(testPart.getSize()).thenReturn(l);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("455");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Execute");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-11-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();
        ServletConfig servletConfig = testUtils.setUp();
        createBattle.init(servletConfig);
        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertEquals("The chosen tournament doesn't exist\r\n", writer.toString());
    }

    @Test
    public void test_tournament_is_not_ongoing() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.txt");
        when(testPart.getInputStream()).thenReturn(testInputStream);
        when(testPart.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"yaml.yaml\"");
        long l = 1;
        when(testPart.getSize()).thenReturn(l);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("4");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Execute");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-11-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();
        ServletConfig servletConfig = testUtils.setUp();
        createBattle.init(servletConfig);
        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        assertEquals("The tournament has not stared yet\r\n", writer.toString());
    }

    @Test
    public void test_user_is_not_educator() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.txt");
        when(testPart.getInputStream()).thenReturn(testInputStream);
        when(testPart.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"yaml.yaml\"");
        long l = 1;
        when(testPart.getSize()).thenReturn(l);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Execute");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-11-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();
        ServletConfig servletConfig = testUtils.setUp();
        createBattle.init(servletConfig);
        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("You can't access to this page\r\n", writer.toString());
    }

    @Test
    public void test_user_is_not_collaborator() throws IOException, ServletException {
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
        Part part = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream inputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("C:\\Users\\antod\\IdeaProjects\\ContiDiPaola\\Code\\src\\main\\java\\it\\polimi\\SE2\\CK\\testElements\\Test.zip");
        when(part.getInputStream()).thenReturn(inputStream);
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"Test.zip\"");

        Part testPart = mock(Part.class);

        // Mock the InputStream of the Part
        InputStream testInputStream = CreateBattleTest.class.getClassLoader().getResourceAsStream("Code/src/main/java/it/polimi/SE2/CK/testElements/yaml.txt");
        when(testPart.getInputStream()).thenReturn(testInputStream);
        when(testPart.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"yaml.yaml\"");
        long l = 1;
        when(testPart.getSize()).thenReturn(l);

        // Set up the request parameters
        when(request.getParameter("TournamentId")).thenReturn("6");
        when(request.getParameter("minStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("maxStudentPerTeamInput")).thenReturn("1");
        when(request.getParameter("battleNameInput")).thenReturn("Execute");
        when(request.getParameter("battleRegistrationDeadlineInput")).thenReturn("2024-10-23T14:00");
        when(request.getParameter("battleSubmissionDeadlineInput")).thenReturn("2024-11-23T14:00");
        when(request.getPart("battleProject")).thenReturn(part);
        when(request.getPart("battleTestCase")).thenReturn(testPart);


        // Create an instance of ShowBattles and invoke the doGet method
        CreateBattle createBattle = new CreateBattle();
        ServletConfig servletConfig = testUtils.setUp();
        createBattle.init(servletConfig);
        createBattle.doPost(request, response);

        // Verify that the response status, content type, and character encoding are set correctly
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("Only collaborator can create battles\r\n", writer.toString());
    }
}