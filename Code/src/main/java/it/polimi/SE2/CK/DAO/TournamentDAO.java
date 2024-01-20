package it.polimi.SE2.CK.DAO;


import it.polimi.SE2.CK.bean.Tournament;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;
import it.polimi.SE2.CK.utils.enumeration.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String query = "SELECT * " +
                "FROM new_schema.t_subscription " +
                "WHERE TournamentID = ? and UserID = ?";
        //statement
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, tournamentID);
            preparedStatement.setInt(2, userID);
            return preparedStatement.execute();
        }
        catch (SQLException e){
            return false;
        }
        finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }
    }

    /**
     * Database search for all educator usernames not in a specific tournament.
     *
     * @param tournamentID the tournament to search.
     * @return list of all educator username not in a tournament.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public List<String> showEducatorNotInTournament(int tournamentID) throws SQLException {
        //search query
        String query = "SELECT u.Username " +
                "FROM user as u " +
                "WHERE u.Role = 0 and not exists (" +
                    "SELECT * " +
                    "FROM t_subscription as ts " +
                    "WHERE ts.UserId = u.idUser and ts.TournamentId = ?)";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<String> result;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, tournamentID);
            resultSet = preparedStatement.executeQuery();

            result = new ArrayList<>();
            while (resultSet.next()){
                result.add(resultSet.getString("Username"));
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
    public boolean addCollaborator(int tournamentID, List<String> collaboratorUsernameList) throws SQLException {
        //insert query
        String query = "INSERT INTO `new_schema`.`t_subscription` " +
                "(`TournamentId`, `UserId`) " +
                "VALUES (?, ?)";
        //statement
        PreparedStatement preparedStatement = null;
        UserDAO userDAO = new UserDAO(con);

        try {
            //start transaction
            con.setAutoCommit(false);

            preparedStatement = con.prepareStatement(query);
            for (String s : collaboratorUsernameList) {
                preparedStatement.setInt(1, tournamentID);
                preparedStatement.setInt(2, userDAO.getUserID(s));
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
}
