package it.polimi.tiw.jdbctest;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;

public class ConnectionTester extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String DB_URL = "jdbc:mysql://localhost:3306/new_schema?serverTimezone=UTC";
        final String USER = "root";
        final String PASS = "Password123*#";
        String result = "Connection worked";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("connessione funziona\n\n");
        } catch (Exception e) {
            result = "Connection failed";
            e.printStackTrace();
        }
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println(result);
        out.close();
    }
}
