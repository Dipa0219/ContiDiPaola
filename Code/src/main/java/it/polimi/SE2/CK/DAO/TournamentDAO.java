package it.polimi.SE2.CK.DAO;


import it.polimi.SE2.CK.bean.Ranking;
import it.polimi.SE2.CK.bean.SessionUser;
import it.polimi.SE2.CK.bean.Tournament;
import it.polimi.SE2.CK.utils.EmailManager;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;
import it.polimi.SE2.CK.utils.enumeration.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class manage the interaction with the database that manage the tournament data.
 */
public class TournamentDAO {
    /**
     * A connection (session) with a specific database.
     */
    private final Connection  con;

    /**
     * TournamentDAO constructor.
     *
     * @param con connection (session) with a specific database.
     */
    public TournamentDAO (Connection con) {
        this.con=con;
    }

    public ArrayList<Tournament> showTournamentByUserId (int userId) throws SQLException {
        ArrayList<Tournament> tournaments = new ArrayList<>();
        String query="select t.idTournament, t.Name, t.Description, t.CreatorId, t.RegDeadline, u.username\n" +
                "from tournament as t join t_subscription on idTournament=TournamentId join user as u on t.CreatorId= u.idUser\n" +
                "where UserId = ?;";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, userId);
            result = pstatement.executeQuery();
            while (result.next()) {
                Tournament tournament= new Tournament();
                tournament.setId(result.getInt("IdTournament"));
                tournament.setName(result.getString("Name"));
                tournament.setDescription(result.getString("Description"));
                tournament.setCreatorId(result.getInt("CreatorId"));
                tournament.setCreatorUsername(result.getString("Username"));
                tournament.setRegDeadline(result.getTimestamp("RegDeadline"));
                tournaments.add(tournament);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }
        return tournaments;
    }

    public Tournament showTournamentById (int id) throws SQLException {
        Tournament tournament = null;
        ArrayList<Tournament> tournaments = new ArrayList<>();
        String query="select t.idTournament, t.Name, t.Description, t.CreatorId, t.RegDeadline, t.Phase, u.username\n" +
                "from tournament as t join user as u on t.CreatorId= u.idUser\n" +
                "where t.idTournament = ?";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, id);
            result = pstatement.executeQuery();
            while (result.next()) {
                tournament= new Tournament();
                tournament.setId(result.getInt("IdTournament"));
                tournament.setName(result.getString("Name"));
                tournament.setDescription(result.getString("Description"));
                tournament.setCreatorId(result.getInt("CreatorId"));
                tournament.setCreatorUsername(result.getString("Username"));
                tournament.setRegDeadline(result.getTimestamp("RegDeadline"));
                tournament.setPhase(result.getString("Phase"));
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }
        return tournament;
    }

    /**
     * Check the existence of a tournament with the specified name.
     *
     * @param name the tournament name to search.
     * @return false if there is no result.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean checkTournamentByName (String name) throws SQLException {
        //search query
        String query = "SELECT * " +
                "FROM new_schema.tournament " +
                "WHERE Name = ?";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            return preparedStatement.execute();
        }
        catch (SQLException e){
            return false;
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e){
                throw new SQLException(e);
            }
        }
    }

    /**
     * Check the user's presence in the specified tournament.
     *
     * @param tournamentID the tournament to check.
     * @param userID the user to check.
     * @return false if there is no result.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean checkUserInTournament(int tournamentID, int userID) throws SQLException {
        //search query
        String query = "select * " +
                "from t_subscription " +
                "where TournamentId = ? and UserId = ?";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, tournamentID);
            preparedStatement.setInt(2, userID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                result = true;
            }
        }
        catch (SQLException e){
            return false;
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }

        return result;
    }

    /**
     * Database search for all educator usernames not in a specific tournament.
     *
     * @param tournamentID the tournament to search.
     * @return list of all educator username not in a tournament.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public ArrayList<SessionUser> showEducatorNotInTournament(int tournamentID) throws SQLException {
        //search query
        String query = "SELECT *" +
                "FROM user as u " +
                "WHERE u.Role = 0 and not exists (" +
                    "SELECT * " +
                    "FROM t_subscription as ts " +
                    "WHERE ts.UserId = u.idUser and ts.TournamentId = ?)";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<SessionUser> result;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, tournamentID);
            resultSet = preparedStatement.executeQuery();

            result = new ArrayList<>();
            while (resultSet.next()){
                SessionUser user = new SessionUser();
                user.setId(resultSet.getInt("idUser"));
                user.setUsername(resultSet.getString("Username"));
                user.setRole(resultSet.getInt("Role"));
                result.add(user);
            }
        }
        catch (SQLException e){
            return null;
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }
        return result;
    }

    /**
     * Inserts a tournament in the database.
     *
     * @param tournament the tournament to insert.
     * @return true if the tournament has been added to the database.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean createTournament(Tournament tournament) throws SQLException {
        //insert query
        String query = "INSERT INTO `tournament` " +
                "(`Name`, `Description`, `CreatorId`, `RegDeadline`, `Phase`) " +
                "VALUES (?, ?, ?, ?, ?)";
        //statement
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, tournament.getName());
            preparedStatement.setString(2, tournament.getDescription());
            preparedStatement.setInt(3, tournament.getCreatorId());
            preparedStatement.setTimestamp(4, tournament.getRegDeadline());
            preparedStatement.setString(5, TournamentState.NOTSTARTED.getValue());
            preparedStatement.execute();
        }
        catch (SQLException e){
            return false;
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e){
                throw new SQLException();
            }
        }
        return true;
    }

    /**
     * Update tournament phase in the database.
     *
     * @param tournamentID the tournament id to update.
     * @return true if the tournament has been closed.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean closeTournament(int tournamentID) throws SQLException {
        //update query
        String query = "UPDATE `new_schema`.`tournament` " +
                "SET `Phase` = ? " +
                "WHERE (`idTournament` = ?)";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, TournamentState.CLOSED.getValue());
            preparedStatement.setInt(2, tournamentID);
            preparedStatement.execute();
        }
        catch (SQLException e){
            return false;
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e){
                throw new SQLException();
            }
        }
        return true;
    }

    /**
     * Insert a collaborator for a tournament in the database.
     *
     * @param tournamentID the tournament selected.
     * @param collaboratorUsernameList the collaborators' username.
     * @return true if the collaborators has been added to the database.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean addCollaborator(int tournamentID, List<Integer> collaboratorUsernameList) throws SQLException {
        //insert query
        String query = "INSERT INTO `new_schema`.`t_subscription` " +
                "(`TournamentId`, `UserId`) " +
                "VALUES (?, ?)";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            //start transaction
            con.setAutoCommit(false);

            preparedStatement = con.prepareStatement(query);
            for (Integer userId : collaboratorUsernameList) {
                preparedStatement.setInt(1, tournamentID);
                preparedStatement.setInt(2, userId);
                preparedStatement.execute();
            }

            //end transaction
            con.commit();
        }
        catch (SQLException e){
            try {
                //transaction error ==> rollback
                con.rollback();
            }
            catch (SQLException e1){
                return false;
            }
            return false;
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e){
                throw new SQLException();
            }
        }


        return true;
    }

    public ArrayList<Tournament> showAllTournamentsByString (String string) throws SQLException {
        ArrayList<Tournament> tournaments = new ArrayList<>();
        String query="select t.idTournament, t.Name, t.Description, t.CreatorId, t.RegDeadline, u.username\n" +
                "from tournament as t join user as u on t.CreatorId = u.idUser;";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            result = pstatement.executeQuery();
            while (result.next()) {
                if(result.getString("Name").contains(string) ){
                    Tournament tournament = new Tournament();
                    tournament.setId(result.getInt("IdTournament"));
                    tournament.setName(result.getString("Name"));
                    tournament.setDescription(result.getString("Description"));
                    tournament.setCreatorId(result.getInt("CreatorId"));
                    tournament.setCreatorUsername(result.getString("Username"));
                    tournament.setRegDeadline(result.getTimestamp("RegDeadline"));
                    tournaments.add(tournament);
                } else if (result.getString("Description")!=null) {
                    if(result.getString("Description").contains(string)){
                        Tournament tournament = new Tournament();
                        tournament.setId(result.getInt("IdTournament"));
                        tournament.setName(result.getString("Name"));
                        tournament.setDescription(result.getString("Description"));
                        tournament.setCreatorId(result.getInt("CreatorId"));
                        tournament.setCreatorUsername(result.getString("Username"));
                        tournament.setRegDeadline(result.getTimestamp("RegDeadline"));
                        tournaments.add(tournament);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }
        return tournaments;
    }

    public boolean joinTournament(int userId, int tournamentId) throws SQLException {
        String query = "select *" +
                "from t_subscription " +
                "where TournamentId=? and UserId=?";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, tournamentId);
            pstatement.setInt(2,userId);
            result = pstatement.executeQuery();
            while (result.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        //insert query
        query = "INSERT INTO `t_subscription` " +
                "(`TournamentId`, `UserId`, `Points`) " +
                "VALUES (?, ?,'0')";
        //statement
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, tournamentId);
            preparedStatement.setInt(2, userId);
            preparedStatement.execute();
        }
        catch (SQLException e){
            return false;
        }
        finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e){
                throw new SQLException();
            }
        }
        return true;
    }

    /**
     * Checks if a tournament has at least one subscribed student.
     *
     * @param tournamentId the interested tournament
     * @return true if at least one student is inscribed.
     */
    private boolean tournamentHaveSubscription(int tournamentId) throws SQLException{
        //search query
        String query = "SELECT * " +
                "FROM user as u " +
                "WHERE u.Role = ? and exists ( " +
                "   SELECT * " +
                "   FROM t_subscription as ts " +
                "   WHERE ts.UserId = u.idUser and ts.TournamentId = ?)";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, UserRole.STUDENT.getValue());
            preparedStatement.setInt(2, tournamentId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                result = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new RuntimeException();
            }
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e2){
                throw new RuntimeException();
            }
        }
        return result;
    }

    /**
     * Searches all tournament Not Started and verify if they can be started.
     */
    public void startTournament() throws SQLException{
        //search query
        String query = "SELECT idTournament, RegDeadline " +
                "FROM tournament " +
                "WHERE Phase = ? " +
                "ORDER BY RegDeadline ASC";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //get the actual date
        java.util.Date currentDate = new Date();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, TournamentState.NOTSTARTED.getValue());
            resultSet = preparedStatement.executeQuery();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            while (resultSet.next()){
                //registration deadline < now
                if (resultSet.getTimestamp("RegDeadline").before(currentTimestamp)){
                    int tournamentId = resultSet.getInt("idTournament");
                    //if tournament do not have any inscribed student
                    if (!tournamentHaveSubscription(tournamentId)){
                        //update tournament table
                        executor.submit(() ->
                                closeTournamentUpdateTable(tournamentId));
                        //send email to all educator that manage the tournament
                        executor.submit(() ->
                                EmailManager.sendEmailToAllCollaboratorInTournamentClosed(tournamentId, con));
                    }
                    else{
                        //update tournament table
                        executor.submit(() ->
                                startTournamentUpdateTable(tournamentId));
                        //send email to all student enrolled to the tournament
                        executor.submit(() ->
                                EmailManager.sendEmailToAllStudentEnrolledInTournamentStarted(tournamentId, con));
                    }
                }
            }
            executor.shutdown();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new RuntimeException();
            }
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e2){
                throw new RuntimeException();
            }
        }
    }

    /**
     * Updates the tournament phase from Not Started to Ongoing.
     *
     * @param tournamentId the tournament id to update.
     */
    private void startTournamentUpdateTable(int tournamentId){
        //update query
        String query = "UPDATE `new_schema`.`tournament` " +
                "SET `Phase` = ? " +
                "WHERE (`idTournament` = ?)";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, TournamentState.ONGOING.getValue());
            preparedStatement.setInt(2, tournamentId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e2){
                throw new RuntimeException();
            }
        }
    }

    /**
     * Updates the tournament phase from Ongoing to Closed.
     *
     * @param tournamentId the tournament id to update.
     */
    private void closeTournamentUpdateTable(int tournamentId){
        //update query
        String query = "UPDATE `new_schema`.`tournament` " +
                "SET `Phase` = ? " +
                "WHERE (`idTournament` = ?)";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, TournamentState.CLOSED.getValue());
            preparedStatement.setInt(2, tournamentId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e2){
                throw new RuntimeException();
            }
        }
    }

    public ArrayList<Ranking> showRanking(int tournamentId) throws SQLException {
        //search query
        String query = "select username, points\n" +
                "from t_subscription join user on UserId = idUser\n" +
                "where points is not null and tournamentId =?\n" +
                "order by points desc;";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Ranking> result;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, tournamentId);
            resultSet = preparedStatement.executeQuery();

            result = new ArrayList<>();
            int i=1;
            while (resultSet.next()){
                Ranking ranking = new Ranking();
                ranking.setName(resultSet.getString("username"));
                ranking.setPoints(resultSet.getInt("points"));
                ranking.setPosition(i);
                i++;
                result.add(ranking);
            }
        }
        catch (SQLException e){
            return null;
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }
        return result;
    }
}
