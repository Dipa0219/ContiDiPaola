package it.polimi.SE2.CK.utils.starting;

import it.polimi.SE2.CK.DAO.BattleDAO;

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
 * This class is responsible for starting battles that have not yet started and have passed the registration deadline, and ending battles that are ongoing and have passed the submission deadline.
 */
public class StartBattle implements ServletContextListener {
    /**
     * Connection object used to connect to the database.
     */
    private Connection connection;

    /**
     * Initializes the servlet context by connecting to the database and scheduling the startBattle and endBattle methods to be called periodically.
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
        scheduledExecutorService.scheduleAtFixedRate(this::startBattle, 0, 1, TimeUnit.MINUTES);
        scheduledExecutorService.scheduleAtFixedRate(this::endBattle, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * Searches for battle that have not started and if the registration deadline has passed it starts them.
     */
    private void startBattle(){
        System.out.println("eccomi qui");
        /*
            TODO
                start battle DAO
         */
    }

    /**
     * Searches for battle that have ongoing and if the submission deadline has passed it starts them.
     */
    private void endBattle(){
        BattleDAO battleDAO = new BattleDAO(connection);

        try {
            battleDAO.closeBattle();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
