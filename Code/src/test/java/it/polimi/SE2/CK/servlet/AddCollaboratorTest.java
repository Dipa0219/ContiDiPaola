package it.polimi.SE2.CK.servlet;


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
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AddCollaboratorTest {

    @Test
    public void test_getUser_doGet() throws ServletException, IOException {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Set the parameters for the request
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Create an instance of LoginManager and invoke the doPost method
        AddCollaborator addCollaborator = new AddCollaborator();

        addCollaborator.doGet(request, response);

        // Verify that the response status is 400
        verify(response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        // Verify that the error message is written to the response
        assertEquals (writer.toString(),"Request non acceptable\r\n");
    }

    @Test
    public void testDoPost() {
    }

    @Test
    public void test_join_tournament_with_empty_session() throws IOException, ServletException {
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
        AddCollaborator addCollaborator = new AddCollaborator();

        addCollaborator.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals(writer.toString(),"You can't access to this page\r\n");
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