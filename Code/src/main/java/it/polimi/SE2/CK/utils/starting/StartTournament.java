package it.polimi.SE2.CK.utils.starting;

import it.polimi.SE2.CK.DAO.TournamentDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for starting tournaments that have not yet started and whose registration deadline has passed.
 */
public class StartTournament implements ServletContextListener {
    /**
     * Connection object used to connect to the database.
     */
    private Connection connection;

    /**
     * Initializes the servlet context by connecting to the database and scheduling the startTournament method to be called periodically.
     *
     * @param sce the servlet context event.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext context = sce.getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        //periodically ping
        scheduledExecutorService.scheduleAtFixedRate(this::startTournament, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * Searches for tournaments that have not started and if the registration deadline has passed it starts them.
     */
    private void startTournament(){
        TournamentDAO tournamentDAO = new TournamentDAO(connection);

        tournamentDAO.startTournament();
    }

    /**
     * Destroy the servlet context.
     *
     * @param sce the servlet context event.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
